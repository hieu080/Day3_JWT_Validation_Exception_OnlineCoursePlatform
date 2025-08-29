package com.day3_jwt_validation_exception_onlinecourseplatform.service;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.EnrollmentRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CourseResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.EnrollmentResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.UserResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse findById(Long id);

    List<EnrollmentResponse> findAll();

    List<EnrollmentResponse> findByStudentId(Long studentId);

    List<EnrollmentResponse> findByCourseId(Long courseId);

    EnrollmentResponse findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<UserResponse> findStudentByCourseId(Long courseId);

    List<CourseResponse> findCourseByStudentId(Long studentId);

    EnrollmentResponse save(EnrollmentRequest enrollmentRequest);

    EnrollmentResponse update(EnrollmentRequest enrollmentRequest, Long id);

    EnrollmentResponse delete(Long id);

    EnrollmentResponse deleteByStudentIdAndCourseId(Long studentId, Long courseId);

}
