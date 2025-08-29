package com.day3_jwt_validation_exception_onlinecourseplatform.controller;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.AuthRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.RefreshTokenRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.RegisterRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.AuthResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return  ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("Đăng kí thành công");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody
            @Valid
            RefreshTokenRequest refreshToken){
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
