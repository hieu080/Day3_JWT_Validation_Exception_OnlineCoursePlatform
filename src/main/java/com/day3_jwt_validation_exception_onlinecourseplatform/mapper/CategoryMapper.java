package com.day3_jwt_validation_exception_onlinecourseplatform.mapper;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CategoryRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CategoryResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Category requestToEntity(CategoryRequest request);

    CategoryResponse entityToResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
