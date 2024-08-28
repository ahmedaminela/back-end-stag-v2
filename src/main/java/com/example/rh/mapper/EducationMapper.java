package com.example.rh.mapper;

import com.example.rh.dto.EducationRequest;
import com.example.rh.dto.EducationResponse;
import com.example.rh.model.Education;

public class EducationMapper {

    public static Education toEntity(EducationRequest request) {
        return Education.builder()
                .institution(request.getInstitution())
                .degree(request.getDegree())
                .fieldOfStudy(request.getFieldOfStudy())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .build();
    }

    public static EducationResponse toResponse(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .institution(education.getInstitution())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .description(education.getDescription())
                .build();
    }
}
