package com.day3_jwt_validation_exception_onlinecourseplatform.mapper;

import com.day3_jwt_validation_exception_onlinecourseplatform.dto.request.CourseRequest;
import com.day3_jwt_validation_exception_onlinecourseplatform.dto.response.CourseResponse;
import com.day3_jwt_validation_exception_onlinecourseplatform.entity.Course;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    Course requestToEntity(CourseRequest request);

    @Mapping(source = "instructor.id", target = "instructorId")
    @Mapping(source = "instructor.fullName", target = "instructorName")
    @Mapping(source = "instructor.email", target = "instructorEmail")
    CourseResponse entityToResponse(Course course);

    List<CourseResponse> toResponseList(List<Course> courses);


}
