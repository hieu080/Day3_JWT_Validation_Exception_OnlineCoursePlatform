package com.day3_jwt_validation_exception_onlinecourseplatform.mapper;


import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.EnrollmentRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.EnrollmentResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Enrollment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface EnrollmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    Enrollment requestToEntity(EnrollmentRequest request);

//    @Mapping(source = "enrollment.student.id", target = "studentId")
//    @Mapping(source = "enrollment.student.fullName", target = "studentName")
//    @Mapping(source = "enrollment.course.id", target = "courseId")
//    @Mapping(source = "enrollment.course.courseName", target = "courseName")
//    @Mapping(source = "enrollment.course.courseCode", target = "courseCode")
    @Mapping(target = "studentName", ignore = true)
    @Mapping(target = "courseName", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "courseCode", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    EnrollmentResponse entityToResponse(Enrollment enrollment);

    @AfterMapping
    default void mapStudentAndCourse(Enrollment enrollment, @MappingTarget EnrollmentResponse response) {
        if (enrollment.getStudent() != null){
            response.setStudentId(enrollment.getStudent().getId());
            response.setStudentName(enrollment.getStudent().getFullName());
        }

        if (enrollment.getCourse() != null){
            response.setCourseId(enrollment.getCourse().getId());
            response.setCourseName(enrollment.getCourse().getCourseName());
            response.setCourseCode(enrollment.getCourse().getCourseCode());
        }
    }

    List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments);

}
