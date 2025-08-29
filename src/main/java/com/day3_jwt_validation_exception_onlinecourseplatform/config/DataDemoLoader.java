package com.day3_jwt_validation_exception_onlinecourseplatform.config;

import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Category;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Course;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.ErrorCode;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import com.day3_jwt_validation_exception_onlinecourseplatform.exception.AppException;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.CategoryRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.CourseRepository;
import com.day3_jwt_validation_exception_onlinecourseplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataDemoLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    @Override
    public void run(String... args){
        if (userRepository.count() == 0){
            userRepository.save(new User(null, "Phan Trung Hieu", "phantrunghieu213@gmail.com", passwordEncoder.encode("123456"), Role.ADMIN, null, null));
            userRepository.save(new User(null, "Nguyễn Văn A", "nguyenvana@gmail.com", passwordEncoder.encode("123456"), Role.INSTRUCTOR, null, null));
            userRepository.save(new User(null, "Trần Văn B", "tranvanb@gmail.com", passwordEncoder.encode("123456"), Role.STUDENT, null, null));
            userRepository.save(new User(null, "Nguyễn Thị H", "nguyenthih@gmail.com", passwordEncoder.encode("123456"), Role.INSTRUCTOR, null, null));
            userRepository.save(new User(null, "Trần Thị C", "tranthic@gmail.com", passwordEncoder.encode("123456"), Role.STUDENT, null, null));
        }

        if (categoryRepository.count() == 0){
            categoryRepository.save(new Category(null, "Hình học", null));
            categoryRepository.save(new Category(null, "Đại số", null));
            categoryRepository.save(new Category(null, "Ngữ văn", null));
            categoryRepository.save(new Category(null, "Sinh học", null));
        }

        if (courseRepository.count() == 0){
            List<Category> categories = categoryRepository.findAllById(List.of(1L, 2L, 3L));
            User instructor = userRepository.findById(2L).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
            courseRepository.save(new Course(null, "Hình học 12", "HINHHOC12", instructor, null, categories, null));
        }
    }
}
