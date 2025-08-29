package com.day3_jwt_validation_exception_onlinecourseplatform.dto.request;

import com.day3_jwt_validation_exception_onlinecourseplatform.validation.groups.OnCreate;
import com.day3_jwt_validation_exception_onlinecourseplatform.validation.groups.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {
    @NotNull(groups = {OnCreate.class}, message = "Id người dùng không được bỏ trống.")
    private Long studentId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Id khóa học không được bỏ trống.")
    private Long courseId;
}
