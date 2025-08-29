package com.day3_jwt_validation_exception_onlinecourseplatform.service;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.UserRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.UserResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;

import java.util.List;

public interface UserService {
    UserResponse findById(Long id);

    UserResponse findByEmail(String email);

    UserResponse findByFullName(String fullName);

    List<UserResponse> findByEmailContaining(String email);

    List<UserResponse> findByFullNameContaining(String fullName);

    List<UserResponse> findByRole(String role);

    List<UserResponse> findAll();

    UserResponse update(UserRequest userRequest, Long id);

    UserResponse delete(Long id);

    User getCurrentUser();
}
