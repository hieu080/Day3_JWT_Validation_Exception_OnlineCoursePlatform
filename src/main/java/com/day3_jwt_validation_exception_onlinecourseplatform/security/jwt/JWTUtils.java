package com.day3_jwt_validation_exception_onlinecourseplatform.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Component
public class JWTUtils {
    @Value("${app.jwt.secret}")
    private String secret;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final  Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(token).getBody();
        return  claimsResolver.apply(claims);
    }
}
