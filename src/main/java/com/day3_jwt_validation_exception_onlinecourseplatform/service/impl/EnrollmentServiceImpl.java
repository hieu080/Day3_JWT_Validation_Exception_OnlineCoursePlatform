package com.day3_jwt_validation_exception_onlinecourseplatform.service.impl;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.EnrollmentRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CourseResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.EnrollmentResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.UserResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Course;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Enrollment;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.mapper.CourseMapper;
import com.day3_jwt_validation_exception_onlinecourseplatform.mapper.EnrollmentMapper;
import com.day3_jwt_validation_exception_onlinecourseplatform.mapper.UserMapper;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.CourseRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.EnrollmentRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.UserRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.EnrollmentService;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;
    private final UserService userService;

    @Override
    public EnrollmentResponse findById(Long id) {
        return enrollmentMapper.entityToResponse(enrollRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND)));
    }

    @Override
    public List<EnrollmentResponse> findAll() {
        return enrollmentMapper.toResponseList(enrollRepository.findAll());
    }

    @Override
    public List<EnrollmentResponse> findByStudentId(Long studentId) {
        return enrollmentMapper.toResponseList(enrollRepository.findByStudentId(studentId));
    }

    @Override
    public List<EnrollmentResponse> findByCourseId(Long courseId) {
        return enrollmentMapper.toResponseList(enrollRepository.findByCourseId(courseId));
    }

    @Override
    public EnrollmentResponse findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentMapper.entityToResponse(enrollRepository.findByStudentIdAndCourseId(studentId, courseId).orElseThrow(() -> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND)));
    }

    @Override
    public List<UserResponse> findStudentByCourseId(Long courseId) {
        return userMapper.toResponseList(enrollRepository.findStudentByCourseId(courseId));
    }

    @Override
    public List<CourseResponse> findCourseByStudentId(Long studentId) {
        return courseMapper.toResponseList(enrollRepository.findCourseByStudentId(studentId));
    }

    @Override
    public EnrollmentResponse save(EnrollmentRequest enrollmentRequest) {
        User student = userRepository.findById(enrollmentRequest.getStudentId()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(enrollmentRequest.getCourseId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Enrollment enrollment = Enrollment.builder().student(student).course(course).build();

        return enrollmentMapper.entityToResponse(enrollRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse update(EnrollmentRequest enrollmentRequest, Long id) {
        Enrollment enrollmentExisting = enrollRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND));

        User currentUser = userService.getCurrentUser();

        boolean isStudentAndOwner = currentUser.getRole().equals(Role.STUDENT)
                && enrollmentExisting.getStudent().getId().equals(currentUser.getId());

        if (!isStudentAndOwner) throw new AppException(ErrorCode.ACCESS_DENIED);

        Course course = courseRepository.findById(enrollmentRequest.getCourseId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        enrollmentExisting.setCourse(course);
        enrollmentExisting.setEnrolledAt(LocalDateTime.now());

        return enrollmentMapper.entityToResponse(enrollRepository.save(enrollmentExisting));
    }

    @Override
    public EnrollmentResponse delete(Long id) {
        Enrollment enrollmentExisting = enrollRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND));

        User currentUser = userService.getCurrentUser();

        boolean isStudentAndOwner = currentUser.getRole().equals(Role.STUDENT)
                && enrollmentExisting.getStudent().getId().equals(currentUser.getId());

        if (!isStudentAndOwner) throw new AppException(ErrorCode.ACCESS_DENIED);

        enrollRepository.delete(enrollmentExisting);

        return enrollmentMapper.entityToResponse(enrollmentExisting);
    }

    @Override
    public EnrollmentResponse deleteByStudentIdAndCourseId(Long studentId, Long courseId) {
        Enrollment enrollmentExisting = enrollRepository.findByStudentIdAndCourseId(studentId, courseId).orElseThrow(()-> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND));

        User currentUser = userService.getCurrentUser();

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        boolean isStudentAndOwner = currentUser.getRole().equals(Role.STUDENT)
                && enrollmentExisting.getStudent().getId().equals(currentUser.getId());

        if (!(isAdmin || isStudentAndOwner)) throw new AppException(ErrorCode.ACCESS_DENIED);

        enrollRepository.delete(enrollmentExisting);

        return enrollmentMapper.entityToResponse(enrollmentExisting);
    }
}
