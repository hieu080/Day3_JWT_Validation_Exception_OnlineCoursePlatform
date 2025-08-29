package com.day3_jwt_validation_exception_onlinecourseplatform.controller;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.EnrollmentRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CourseResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.EnrollmentResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.UserResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.EnrollmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enroll")
@RequiredArgsConstructor
public class EnrollmentController {
    private  final EnrollmentService enrollmentService;

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> findById(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id){
        return  ResponseEntity.ok(enrollmentService.findById(id));
    }

    @GetMapping
    public  ResponseEntity<List<EnrollmentResponse>> findAll(){
        return   ResponseEntity.ok(enrollmentService.findAll());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> findByStudentId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long studentId){
        return   ResponseEntity.ok(enrollmentService.findByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> findByCourseId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long courseId){
        return   ResponseEntity.ok(enrollmentService.findByCourseId(courseId));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public  ResponseEntity<EnrollmentResponse> findByStudentIdAndCourseId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long studentId,
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long courseId){
        return ResponseEntity.ok(enrollmentService.findByStudentIdAndCourseId(studentId, courseId));
    }

    @GetMapping("/search/student/{courseId}")
    public  ResponseEntity<List<UserResponse>> findStudentByCourseId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long courseId){
        return   ResponseEntity.ok(enrollmentService.findStudentByCourseId(courseId));
    }

    @GetMapping("/search/course/{studentId}")
    public   ResponseEntity<List<CourseResponse>> findCourseByStudentId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long studentId){
        return    ResponseEntity.ok(enrollmentService.findCourseByStudentId(studentId));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<EnrollmentResponse> create(@Valid @RequestBody EnrollmentRequest enrollmentRequest){
        return ResponseEntity.ok(enrollmentService.save(enrollmentRequest));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PutMapping("/update/{id}")
    public ResponseEntity<EnrollmentResponse> update(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id,
            @Valid
            @RequestBody
            EnrollmentRequest enrollmentRequest){
        return ResponseEntity.ok(enrollmentService.update(enrollmentRequest, id));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EnrollmentResponse> delete(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long id){
        return  ResponseEntity.ok(enrollmentService.delete(id));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/delete/student/{studentId}/course/{courseId}")
    public  ResponseEntity<EnrollmentResponse> deleteByStudentIdAndCourseId(
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long studentId,
            @PathVariable
            @NotNull(message = "Id không được bỏ trống")
            @Positive(message = "Id phải lớn hơn 0")
            Long courseId){
        return  ResponseEntity.ok(enrollmentService.deleteByStudentIdAndCourseId(studentId, courseId));
    }
}
