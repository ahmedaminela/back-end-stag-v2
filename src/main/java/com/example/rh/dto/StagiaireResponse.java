package com.example.rh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StagiaireResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String rhName;         // Name of the RH
    private String stageTitle;     // Title of the Stage
    private String encadrantName;
    private Date startDate;        // Start date of the stage
    private Date endDate; // Name of the Encadrant
    private Long applicationId;    // Add this line for the application ID

}
