package com.day3_jwt_validation_exception_onlinecourseplatform.security;

import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy user có email: "+ email));

        return new CustomUserDetails(user);
    }
}
