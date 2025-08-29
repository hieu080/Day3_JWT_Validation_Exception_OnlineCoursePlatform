package com.day3_jwt_validation_exception_onlinecourseplatform.service.impl;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CourseRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CourseResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Category;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Course;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.mapper.CourseMapper;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.CategoryRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.CourseRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.UserRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.CourseService;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private  final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Override
    public CourseResponse findByCourseName(String name) {
        return courseMapper.entityToResponse(courseRepository.findByCourseName(name).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND)));
    }

    @Override
    public CourseResponse findByCourseCode(String code) {
        return courseMapper.entityToResponse(courseRepository.findByCourseCode(code).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND)));
    }

    @Override
    public List<CourseResponse> findAll() {
        return courseMapper.toResponseList(courseRepository.findAll());
    }

    @Override
    public CourseResponse findById(Long id) {
        return courseMapper.entityToResponse(courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND)));
    }

    @Override
    public List<CourseResponse> findByCourseNameContainingIgnoreCase(String name) {
        return courseMapper.toResponseList(courseRepository.findByCourseNameContainingIgnoreCase(name));
    }

    @Override
    public List<CourseResponse> findByCategoryId(Long id) {
        return courseMapper.toResponseList(courseRepository.findByCategoryId(id));
    }

    @Override
    public List<CourseResponse> findByInstructorId(Long id) {
        return courseMapper.toResponseList(courseRepository.findByInstructorId(id));
    }

    @Override
    public CourseResponse save(CourseRequest courseRequest) {
        if (courseRepository.existsByCourseCode(courseRequest.getCourseCode())) {
            throw new AppException(ErrorCode.DUPLICATE_KEY);
        }

        User currentUser = userService.getCurrentUser();

        boolean isInstructor = currentUser.getRole().equals(Role.INSTRUCTOR);
        if (!isInstructor) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        List<Long> ids = courseRequest.getCategoryIds();
        List<Category> categories = categoryRepository.findAllById(ids);

        if (categories.size() != ids.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Course course = Course.builder().courseName(courseRequest.getCourseName())
                .courseCode(courseRequest.getCourseCode())
                .instructor(currentUser)
                .categories(categories)
                .build();

        return courseMapper.entityToResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse update(CourseRequest courseRequest, Long id) {
        Course courseExisting = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        if (courseRepository.existsByCourseCodeAndIdNot(courseRequest.getCourseCode(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_KEY);
        }

        User currentUser = userService.getCurrentUser();

        boolean isInstructorAndOwner = currentUser.getRole().equals(Role.INSTRUCTOR)
                && courseExisting.getInstructor().getId().equals(currentUser.getId());

        if (!isInstructorAndOwner) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        List<Long> ids = courseRequest.getCategoryIds();
        List<Category> categories = categoryRepository.findAllById(ids);

        if (categories.size() != ids.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        courseExisting.setCourseName(courseRequest.getCourseName());
        courseExisting.setCourseCode(courseRequest.getCourseCode());

        courseExisting.getCategories().clear();
        courseExisting.getCategories().addAll(categories);

        return courseMapper.entityToResponse(courseRepository.save(courseExisting));
    }

    @Override
    public CourseResponse delete(Long id) {
        Course courseExisting = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        User currentUser = userService.getCurrentUser();

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isInstructorAndOwner = currentUser.getRole().equals(Role.INSTRUCTOR)
                && courseExisting.getInstructor().getId().equals(currentUser.getId());

        if (!isAdmin && !isInstructorAndOwner) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        courseRepository.delete(courseExisting);
        return courseMapper.entityToResponse(courseExisting);
    }
}
