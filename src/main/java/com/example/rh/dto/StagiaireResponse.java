package com.example.rh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long rhId;
    private Long stageId;
    private Long encadrantId;
}
