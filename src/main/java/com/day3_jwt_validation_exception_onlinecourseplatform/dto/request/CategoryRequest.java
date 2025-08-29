package com.day3_jwt_validation_exception_onlinecourseplatform.dto.request;

import com.day3_jwt_validation_exception_onlinecourseplatform.validation.groups.OnCreate;
import com.day3_jwt_validation_exception_onlinecourseplatform.validation.groups.OnUpdate;
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
public class CategoryRequest {
    @NotBlank(groups = {OnCreate.class}, message = "Tên phân loại là bắt buộc.")
    @Size(min = 6, max = 50, message = "Tên phân loại phải có từ 6 - 50 kí tự.")
    private String categoryName;
}
