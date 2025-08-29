package com.day3_jwt_validation_exception_onlinecourseplatform.controller;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.UserRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.UserResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.UserService;
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable
            @NotNull(message = "Id không được trống.")
            @Positive(message = "Id phải lớn hơn 0.")
            Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    public  ResponseEntity<UserResponse> findByEmail(@PathVariable @NotBlank(message = "Từ khóa không được bỏ trống") String email){
        return  ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/fullName/{fullName}")
    public  ResponseEntity<UserResponse> findByFullName(@PathVariable @NotBlank(message = "Từ khóa không được bỏ trống") String fullName){
        return   ResponseEntity.ok(userService.findByFullName(fullName));
    }

    @GetMapping("/search/email/{email}")
    public ResponseEntity<List<UserResponse>> searchByEmail(@PathVariable @NotBlank(message = "Từ khóa không được bỏ trống") String email){
        return ResponseEntity.ok(userService.findByEmailContaining(email));
    }

    @GetMapping("/search/fullName/{fullName}")
    public ResponseEntity<List<UserResponse>> searchByFullName(@PathVariable @NotBlank(message = "Từ khóa không được bỏ trống") String fullName){
        return ResponseEntity.ok(userService.findByFullNameContaining(fullName));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/role/{role}")
    public  ResponseEntity<List<UserResponse>> findByRole(@PathVariable @NotBlank(message = "Role không được bỏ trống") String role){
        return  ResponseEntity.ok(userService.findByRole(role));
    }

    @GetMapping()
    public  ResponseEntity<List<UserResponse>> findAll(){
        return  ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/update/{id}")
    public   ResponseEntity<UserResponse> update(
            @PathVariable
            @NotNull(message = "Id không được trống.")
            @Positive(message = "Id phải lớn hơn 0.")
            Long id,
            @Valid
            @RequestBody
            UserRequest userRequest){
        return ResponseEntity.ok(userService.update(userRequest, id));
    }

    @DeleteMapping("/delete/{id}")
    public    ResponseEntity<UserResponse> delete(
            @PathVariable
            @NotNull(message = "Id không được trống.")
            @Positive(message = "Id phải lớn hơn 0.")
            Long id){
        return  ResponseEntity.ok(userService.delete(id));
    }

}
