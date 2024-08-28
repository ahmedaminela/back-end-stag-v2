package com.example.rh.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StageCreateRequest {
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Long encadrantId;
}
