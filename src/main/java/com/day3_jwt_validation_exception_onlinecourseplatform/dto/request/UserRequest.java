package com.day3_jwt_validation_exception_onlinecourseplatform.dto.request;

import com.day3_jwt_validation_exception_onlinecourseplatform.validation.groups.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(groups = {OnCreate.class}, message = "Tên người dùng không được để trống.")
    @Size(min = 6, max = 100, message = "Tên người dùng phải có từ 6 - 100 kí tự.")
    private String fullName;

    @NotBlank(groups = {OnCreate.class}, message = "Password không được để trống.")
    @Size(min = 6, max = 50, message = "Password phải có từ 6 - 50 kí tự.")
    private String password;
}
