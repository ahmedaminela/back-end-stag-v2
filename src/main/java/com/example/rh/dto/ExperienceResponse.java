package com.example.rh.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ExperienceResponse {
    private Long id;
    private String company;
    private String position;
    private Date startDate;
    private Date endDate;
    private String description;
}