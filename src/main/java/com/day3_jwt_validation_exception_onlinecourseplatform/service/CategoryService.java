package com.day3_jwt_validation_exception_onlinecourseplatform.service;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CategoryRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse save(CategoryRequest categoryRequest);
    CategoryResponse update(CategoryRequest categoryRequest, Long id);
    CategoryResponse delete(Long id);
    CategoryResponse findById(Long id);
    List<CategoryResponse> findAll();
    CategoryResponse findByName(String name);
    List<CategoryResponse> findByNameContainingIgnoreCase(String keyword);
    List<CategoryResponse> findByCourseId(Long courseId);
}
