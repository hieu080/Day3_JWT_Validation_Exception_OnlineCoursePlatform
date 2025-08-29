package com.day3_jwt_validation_exception_onlinecourseplatform.dto.response;

import java.time.Instant;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Instant expireAt,
        Long userId,
        String fullName,
        String email,
        String role) {
}
