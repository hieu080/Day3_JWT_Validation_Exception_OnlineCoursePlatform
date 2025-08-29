package com.day3_jwt_validation_exception_onlinecourseplatform.dto.response;

import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Course;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Enrollment;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private  String email;
    private String role;
    List<Course> courses;
    List<Enrollment> enrollments;
}
