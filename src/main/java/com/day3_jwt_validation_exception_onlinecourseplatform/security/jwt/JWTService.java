package com.day3_jwt_validation_exception_onlinecourseplatform.security.jwt;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.RefreshTokenRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.AuthResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.UserRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.security.CustomUserDetails;
import com.day3_jwt_validation_exception_onlinecourseplatform.security.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JWTService {
    private final Key key;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    public JWTService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration}") long accessExpirationMs,
                      @Value("${app.jwt.refresh-expiration}") long refreshExpirationMs,
                      UserRepository userRepository,
                      CustomUserDetailsService userDetailsService) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessToken(UserDetails user){
        return  Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails user){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Instant getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.toInstant();
    }

    public boolean validateToken(String token, UserDetails user) {
        try {
            String username = extractUsername(token);
            return username.equals(user.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // parse = hợp lệ + chưa hết hạn
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token không hợp lệ hoặc đã hết hạn");
        }

        // Lấy email/username từ refresh token
        String email = extractUsername(refreshToken);

        // Lấy entity để trả về id/fullName/email (và để build CustomUserDetails)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy user"));

        CustomUserDetails cud = new CustomUserDetails(user);

        // Chính sách: rotate refresh token (cấp token mới)
        String newAccessToken = generateAccessToken(cud);
        String newRefreshToken = generateRefreshToken(cud);

        String roles = cud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                getExpiration(newAccessToken),
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                roles
        );
    }

}
