package com.example.rh.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ExperienceRequest {
    private String company;
    private String position;
    private Date startDate;
    private Date endDate;
    private String description;
}