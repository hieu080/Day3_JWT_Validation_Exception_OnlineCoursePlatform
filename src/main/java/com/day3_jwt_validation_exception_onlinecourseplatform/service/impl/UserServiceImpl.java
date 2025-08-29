package com.day3_jwt_validation_exception_onlinecourseplatform.service.impl;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.UserRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.UserResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.mapper.UserMapper;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.UserRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.UserService;
import com.day3_jwt_validation_exception_onlinecourseplatform.validation.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse findById(Long id) {

        return userMapper.entityToResponse(userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public UserResponse findByEmail(String email) {
        ValidatorUtils.isValidEmail(email);

        return userMapper.entityToResponse(userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public UserResponse findByFullName(String fullName) {

        return userMapper.entityToResponse(userRepository.findByFullName(fullName).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public List<UserResponse> findByEmailContaining(String email) {

        return userMapper.toResponseList(userRepository.findByEmailContaining(email));
    }

    @Override
    public List<UserResponse> findByFullNameContaining(String fullName) {

        return userMapper.toResponseList(userRepository.findByFullNameContaining(fullName));
    }

    @Override
    public List<UserResponse> findByRole(String role) {
        ValidatorUtils.isValidRole(role);

        return userMapper.toResponseList(userRepository.findByRole(Role.valueOf(role.toUpperCase())));
    }

    @Override
    public List<UserResponse> findAll() {

        return userMapper.toResponseList(userRepository.findAll());
    }

    @Override
    public UserResponse update(UserRequest userRequest, Long id) {
        User userExisting = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        User currentUser = getCurrentUser();

        boolean isOwner = currentUser.getId().equals(userExisting.getId());

        if (!isOwner) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        userExisting.setFullName(userRequest.getFullName());
        userExisting.setPassword(userRequest.getPassword());

        return userMapper.entityToResponse(userRepository.save(userExisting));
    }

    @Override
    public UserResponse delete(Long id) {
        User userExisting = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        User currentUser = getCurrentUser();

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isOwner = currentUser.getId().equals(userExisting.getId());

        if (!(isAdmin || isOwner)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return userMapper.entityToResponse(userExisting);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String email = authentication.getName(); // lấy username từ token
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

}
