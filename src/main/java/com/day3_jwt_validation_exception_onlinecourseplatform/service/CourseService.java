package com.day3_jwt_validation_exception_onlinecourseplatform.service;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CourseRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CourseResponse;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    CourseResponse findByCourseName(String name);

    CourseResponse findByCourseCode(String code);

    List<CourseResponse> findAll();

    CourseResponse findById(Long id);

    List<CourseResponse> findByCourseNameContainingIgnoreCase(String name);

    List<CourseResponse> findByCategoryId(Long id);

    List<CourseResponse> findByInstructorId(Long id);

    CourseResponse save(CourseRequest courseRequest);

    CourseResponse update(CourseRequest courseRequest, Long id);

    CourseResponse delete(Long id);
}
