package com.day3_jwt_validation_exception_onlinecourseplatform.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String categoryName;
}
