package com.day3_jwt_validation_exception_onlinecourseplatform.controller;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CategoryRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CategoryResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private  final CategoryService categoryService;

    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable
            @NotNull(message = "Id không được trống.")
            @Positive(message = "Id phải lớn hơn 0.")
            Long id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponse> findByName(
            @PathVariable
            @NotBlank(message = "Từ khóa không được trống.")
            String name){
        return ResponseEntity.ok(categoryService.findByName(name));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryResponse>> findByKeyword(
            @PathVariable
            @NotBlank(message = "Từ khóa không được trống.")
            String keyword){
        return ResponseEntity.ok(categoryService.findByNameContainingIgnoreCase(keyword));
    }

    @GetMapping("/courseId/{courseId}")
    public ResponseEntity<List<CategoryResponse>> findByCourseId(
            @PathVariable
            @NotNull(message = "Id không được trống.")
            @Positive(message = "Id phải lớn hơn 0.")
            Long courseId){
        return ResponseEntity.ok(categoryService.findByCourseId(courseId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok(categoryService.save(categoryRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponse> update(@Valid @RequestBody CategoryRequest categoryRequest,@PathVariable Long id){
        return ResponseEntity.ok(categoryService.update(categoryRequest,id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CategoryResponse> delete(
            @PathVariable
            @NotNull(message = "Id không được trống.")
            @Positive(message = "Id phải lớn hơn 0.")
            Long id){
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
