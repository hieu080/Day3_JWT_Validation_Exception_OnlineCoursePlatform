package com.day3_jwt_validation_exception_onlinecourseplatform.controller;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CourseRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CourseResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private  final CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> findById(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id){
        return  ResponseEntity.ok(courseService.findById(id));
    }

    @GetMapping
    public  ResponseEntity<List<CourseResponse>> findAll(){
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/courseName/{courseName}")
    public ResponseEntity<CourseResponse> findByCourseName(@PathVariable @NotNull(message = "Từ khóa không được bỏ trống") String courseName){
        return ResponseEntity.ok(courseService.findByCourseName(courseName));
    }

    @GetMapping("/courseCode/{courseCode}")
    public ResponseEntity<CourseResponse> findByCourseCode(@PathVariable @NotNull(message = "Từ khóa không được bỏ trống") String courseCode){
        return ResponseEntity.ok(courseService.findByCourseCode(courseCode));
    }

    @GetMapping("search/{courseName}")
    public ResponseEntity<List<CourseResponse>> findByCourseNameContainingIgnoreCase(@PathVariable @NotNull(message = "Từ khóa không được bỏ trống") String courseName){
        return  ResponseEntity.ok(courseService.findByCourseNameContainingIgnoreCase(courseName));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<CourseResponse>> findByCategoryId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id){
        return  ResponseEntity.ok(courseService.findByCategoryId(id));
    }

    @GetMapping("/instructor/{id}")
    public  ResponseEntity<List<CourseResponse>> findByInstructorId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id){
        return   ResponseEntity.ok(courseService.findByInstructorId(id));
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PostMapping("/create")
    public  ResponseEntity<CourseResponse> save(@Valid @RequestBody CourseRequest courseRequest){
        return ResponseEntity.ok(courseService.save(courseRequest));
    }

    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CourseResponse> update(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id,
            @Valid @RequestBody CourseRequest courseRequest){
        return  ResponseEntity.ok(courseService.update(courseRequest, id));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<CourseResponse> delete(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id){
        return   ResponseEntity.ok(courseService.delete(id));
    }
}
