package com.day3_jwt_validation_exception_onlinecourseplatform.repository;

import com.day3_jwt_validation_exception_onlinecourseplatform.entity.User;
import com.day3_jwt_validation_exception_onlinecourseplatform.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByFullName(String fullName);

    List<User> findByEmailContaining(String email);

    List<User> findByFullNameContaining(String fullName);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") Role role);
}
