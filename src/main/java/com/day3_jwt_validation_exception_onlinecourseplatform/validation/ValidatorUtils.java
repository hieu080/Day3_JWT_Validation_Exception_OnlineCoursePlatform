package com.day3_jwt_validation_exception_onlinecourseplatform.validation;

import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;

import java.util.regex.Pattern;

public class ValidatorUtils {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static void isValidEmail(String email){
        if (email == null || email.isBlank()) {
            throw new AppException(ErrorCode.NOT_EMPTY, "Email không được để trống.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new AppException(ErrorCode.INVALID_EMAIL, "Email không hợp lệ.");
        }
    }

    public static void isValidRole(String role){
        try {
            Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }
    }
}
