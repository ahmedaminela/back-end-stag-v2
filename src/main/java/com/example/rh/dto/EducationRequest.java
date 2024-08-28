package com.example.rh.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EducationRequest {
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private Date startDate;
    private Date endDate;
    private String description;
}