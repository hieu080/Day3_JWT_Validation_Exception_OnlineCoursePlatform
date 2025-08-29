package com.day3_jwt_validation_exception_onlinecourseplatform.service;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.AuthRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.RefreshTokenRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.RegisterRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.AuthResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.UserRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.security.CustomUserDetails;
import com.day3_jwt_validation_exception_onlinecourseplatform.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest authRequest) {
        if (authRequest == null){
            return  null;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.email(), authRequest.password()
                )
        );
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);

        // convert authorities sang String
        String roles = customUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getExpiration(accessToken),
                customUserDetails.getId(),
                customUserDetails.getFullName(),
                customUserDetails.getEmail(),
                roles
        );
    }

    public void register(RegisterRequest registerRequest) {
        if (registerRequest == null){
            return ;
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())){
            throw new AppException(ErrorCode.DUPLICATE_KEY);
        }

        // Mặc định chỉ cho phép STUDENT hoặc INSTRUCTOR
        Role role = Role.STUDENT; // mặc định
        // Nếu muốn cho INSTRUCTOR từ frontend thì có thể validate danh sách trắng
        if (registerRequest.getRole() != null && registerRequest.getRole().equalsIgnoreCase("INSTRUCTOR")) {
            role = Role.INSTRUCTOR;
        }

        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshToken) {
        return jwtService.refreshToken(refreshToken.getRefreshToken());
    }

}
