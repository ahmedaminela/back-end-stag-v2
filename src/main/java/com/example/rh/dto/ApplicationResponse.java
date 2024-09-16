package com.example.rh.dto;

import com.example.rh.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long stagiaireId;
    private String stagiaireFirstname;
    private String stagiaireLastname;
    private Long stageId;
    private String stageName;
    private Date submissionDate;
    private ApplicationStatus status;
    private String notes;
    private List<FileResponse> files;
}
