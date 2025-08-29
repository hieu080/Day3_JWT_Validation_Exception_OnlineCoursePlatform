package com.day3_jwt_validation_exception_onlinecourseplatform.repository;

import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseName(String name);

    Optional<Course> findByCourseCode(String code);

    boolean existsByCourseCode(String code);

    boolean existsByCourseCodeAndIdNot(String code, Long id);

    List<Course> findByCourseNameContainingIgnoreCase(String name);

    @Query("SELECT co FROM Course co JOIN Category c WHERE c.id = :categoryId")
    List<Course> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Course> findByInstructorId(Long instructorId);
}
