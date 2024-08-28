package com.example.rh.dto;

import com.example.rh.enums.StageState;
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
public class StageResponse {
    private Long id;
    private String title;
    private Date startDate;
    private Date endDate;
    private String description;
    private StageState state;
    private RhResponse rh;
    private EncadrantResponse encadrant;
    private List<ApplicationResponse> applications;
    private List<StagiaireResponse> stagiaires;
}
