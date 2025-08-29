package com.day3_jwt_validation_exception_onlinecourseplatform.service.impl;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CategoryRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CategoryResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Category;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.mapper.CategoryMapper;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.CategoryRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse save(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByCategoryName(categoryRequest.getCategoryName())) {
            throw new AppException(ErrorCode.DUPLICATE_KEY);
        }

        Category category = categoryMapper.requestToEntity(categoryRequest);

        return categoryMapper.entityToResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryRequest, Long id) {
        if (categoryRepository.existsByCategoryNameAndIdNot(categoryRequest.getCategoryName(), id)) {
            throw new AppException(ErrorCode.DUPLICATE_KEY);
        }

        Category categoryExisting = categoryRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryExisting.setCategoryName(categoryRequest.getCategoryName());

        return categoryMapper.entityToResponse(categoryRepository.save(categoryExisting));
    }

    @Override
    public CategoryResponse delete(Long id) {
        Category categoryExisting = categoryRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(categoryExisting);

        return categoryMapper.entityToResponse(categoryExisting);
    }

    @Override
    public CategoryResponse findById(Long id) {
        return categoryMapper.entityToResponse(categoryRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }

    @Override
    public CategoryResponse findByName(String name) {
        return categoryMapper.entityToResponse(categoryRepository.findByCategoryName(name).orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    @Override
    public List<CategoryResponse> findByNameContainingIgnoreCase(String keyword) {
        return categoryMapper.toResponseList(categoryRepository.findByCategoryNameContainingIgnoreCase(keyword));
    }

    @Override
    public List<CategoryResponse> findByCourseId(Long courseId) {
        return categoryMapper.toResponseList(categoryRepository.findByCourseId(courseId));
    }


}
