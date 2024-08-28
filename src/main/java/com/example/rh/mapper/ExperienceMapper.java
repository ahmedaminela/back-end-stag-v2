package com.example.rh.mapper;

import com.example.rh.dto.ExperienceRequest;
import com.example.rh.dto.ExperienceResponse;
import com.example.rh.model.Experience;

public class ExperienceMapper {

    public static Experience toEntity(ExperienceRequest request) {
        return Experience.builder()
                .company(request.getCompany())
                .position(request.getPosition())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .build();
    }

    public static ExperienceResponse toResponse(Experience experience) {
        return ExperienceResponse.builder()
                .id(experience.getId())
                .company(experience.getCompany())
                .position(experience.getPosition())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .description(experience.getDescription())
                .build();
    }
}
