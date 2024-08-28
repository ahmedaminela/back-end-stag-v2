package com.example.rh.dto;

import com.example.rh.model.File;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileRequest {
    private File profilePicture;
    private List<EducationRequest> educations;
    private List<ExperienceRequest> experiences;
}
