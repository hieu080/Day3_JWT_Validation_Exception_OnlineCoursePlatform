package com.day3_jwt_validation_exception_onlinecourseplatform.mapper;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.UserRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.UserResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "role", ignore = true)
    User requestToEntity(UserRequest request);

    UserResponse entityToResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
