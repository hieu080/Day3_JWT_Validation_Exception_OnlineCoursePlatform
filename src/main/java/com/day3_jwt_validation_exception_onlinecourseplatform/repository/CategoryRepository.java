package com.day3_jwt_validation_exception_onlinecourseplatform.repository;

import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String name);

    List<Category> findAllById(Long id);

    boolean existsByCategoryName(String name);

    boolean existsByCategoryNameAndIdNot(String categoryName, Long id);

    List<Category> findByCategoryNameContainingIgnoreCase(String keyword);

    @Query("SELECT c FROM Category c JOIN c.courses co WHERE co.id = :courseId")
    List<Category> findByCourseId(@Param("courseId") Long courseId);

}
