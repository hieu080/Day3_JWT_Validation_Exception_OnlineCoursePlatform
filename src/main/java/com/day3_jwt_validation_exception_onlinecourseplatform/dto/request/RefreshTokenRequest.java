package com.day3_jwt_validation_exception_onlinecourseplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "RefreshToken không được bỏ trống")
    private String refreshToken;
}
