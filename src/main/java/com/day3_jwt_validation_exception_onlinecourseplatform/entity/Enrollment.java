package com.day3_jwt_validation_exception_onlinecourseplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime enrolledAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private  User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @PrePersist
    private  void prePersist() {
        enrolledAt = LocalDateTime.now();
    }
}