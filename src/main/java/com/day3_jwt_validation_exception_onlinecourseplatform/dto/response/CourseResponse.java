package com.day3_jwt_validation_exception_onlinecourseplatform.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CourseResponse {
    private Long id;
    private String courseName;
    private String courseCode;
    private Long instructorId;
    private String instructorName;
    private String instructorEmail;
    private LocalDateTime createdDate;
    List<CategoryResponse> categories;
}
