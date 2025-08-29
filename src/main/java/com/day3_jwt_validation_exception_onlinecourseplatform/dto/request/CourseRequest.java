package com.day3_jwt_validation_exception_onlinecourseplatform.dto.request;

import com.day3_jwt_validation_exception_onlinecourseplatform.validation.groups.OnCreate;
import com.day3_jwt_validation_exception_onlinecourseplatform.validation.groups.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    @NotBlank(groups = {OnCreate.class}, message = "Tên khóa học là bắt buộc.")
    @Size(min = 6, max = 100, message = "Tên khóa học phải có từ 6 - 100 kí tự.")
    private String courseName;

    @NotBlank(groups = {OnCreate.class}, message = "Mã khóa học là bắt buộc.")
    @Size(min = 4, max = 10, message = "Mã khóa học phải có từ 4 - 10 kí tự.")
    private String courseCode;

    @NotEmpty(groups = {OnCreate.class}, message = "Phải có ít nhất 1 phân loại cho khóa học.")
    private List<Long> categoryIds;
}
