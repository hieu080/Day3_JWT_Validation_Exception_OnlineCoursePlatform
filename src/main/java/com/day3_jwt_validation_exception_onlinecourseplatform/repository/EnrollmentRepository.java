package com.day3_jwt_validation_exception_onlinecourseplatform.repository;

import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Course;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Enrollment;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT e.course FROM Enrollment e WHERE e.student.id = :studentId")
    List<Course> findCourseByStudentId(@Param("studentId") Long stundentId);

    @Query("SELECT e.student FROM Enrollment e WHERE e.course.id = :courseId")
    List<User> findStudentByCourseId(@Param("courseId") Long courseId);
}
