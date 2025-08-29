package com.day3_jwt_validation_exception_onlinecourseplatform.exception;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.ErrorResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(ex.getHttpStatus())
                .errorCode(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .traceId(MDC.get("traceId")) // nếu có tích hợp logging
                .details(ex.getDetails())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {

        log.error("Unexpected error", ex);

        String message = ex.getMessage();
        if (message == null && ex.getCause() != null) {
            message = ex.getCause().getMessage();
        }

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(ErrorCode.SYSTEM_ERROR.getHttpStatus().value())
                .errorCode(ErrorCode.SYSTEM_ERROR.getCode())
                .message(ex.getMessage())
                .traceId(MDC.get("traceId"))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(ErrorCode.SYSTEM_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        Map<String, Object> details = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(ErrorCode.VALIDATION_FAILED.getHttpStatus().value())
                .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
                .message(ErrorCode.VALIDATION_FAILED.getDefaultMessage())
                .details(details)
                .traceId(MDC.get("traceId"))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest request) {
        Map<String, Object> details = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage));

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(ErrorCode.VALIDATION_FAILED.getHttpStatus().value())
                .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
                .message(ErrorCode.VALIDATION_FAILED.getDefaultMessage())
                .details(details)
                .traceId(MDC.get("traceId"))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.getHttpStatus()).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                                                             HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(ErrorCode.DATA_INTEGRITY_VIOLATION.getHttpStatus().value())
                .errorCode(ErrorCode.DATA_INTEGRITY_VIOLATION.getCode())
                .message(ErrorCode.DATA_INTEGRITY_VIOLATION.getDefaultMessage())
                .traceId(MDC.get("traceId"))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ErrorCode.DATA_INTEGRITY_VIOLATION.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(ErrorCode.FORBIDDEN.getHttpStatus().value())
                .errorCode(ErrorCode.FORBIDDEN.getCode())
                .message(ErrorCode.FORBIDDEN.getDefaultMessage())
                .traceId(MDC.get("traceId"))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ErrorCode.FORBIDDEN.getHttpStatus()).body(response);
    }


}
