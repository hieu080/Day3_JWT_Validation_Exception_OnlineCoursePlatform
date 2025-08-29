package com.day3_jwt_validation_exception_onlinecourseplatform.mapper;


import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.EnrollmentRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.EnrollmentResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Enrollment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    Enrollment requestToEntity(EnrollmentRequest request);

    @Mapping(source = "enrollment.student.id", target = "studentId")
    @Mapping(source = "enrollment.student.fullName", target = "studentName")
    @Mapping(source = "enrollment.course.id", target = "courseId")
    @Mapping(source = "enrollment.course.courseName", target = "courseName")
    @Mapping(source = "enrollment.course.courseCode", target = "courseCode")
    EnrollmentResponse entityToResponse(Enrollment enrollment);

    List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments);

}
