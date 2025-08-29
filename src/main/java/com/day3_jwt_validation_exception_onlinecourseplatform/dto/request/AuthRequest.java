package com.day3_jwt_validation_exception_onlinecourseplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Email không được để trống.")
        @Size(min = 6, max = 50, message = "Email phải có từ 6 - 50 kí tự.")
        String email,

        @NotBlank(message = "Password không được để trống.")
        @Size(min = 6, max = 50, message = "Password phải có từ 6 - 50 kí tự.")
        String password){

}
