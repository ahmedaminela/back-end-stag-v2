package com.example.rh.dto;

import com.example.rh.model.File;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private File profilePicture;
    private List<EducationResponse> educations;
    private List<ExperienceResponse> experiences;
}