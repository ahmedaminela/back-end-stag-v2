package com.example.rh.mapper;

import com.example.rh.dto.ProfileRequest;
import com.example.rh.dto.ProfileResponse;
import com.example.rh.model.Profile;

import java.util.stream.Collectors;

public class ProfileMapper {

    public static Profile toEntity(ProfileRequest request) {
        return Profile.builder()
                .profilePicture(request.getProfilePicture())
                .educations(request.getEducations().stream().map(EducationMapper::toEntity).collect(Collectors.toList()))
                .experiences(request.getExperiences().stream().map(ExperienceMapper::toEntity).collect(Collectors.toList()))
                .build();
    }

    public static ProfileResponse toResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .profilePicture(profile.getProfilePicture())
                .educations(profile.getEducations().stream().map(EducationMapper::toResponse).collect(Collectors.toList()))
                .experiences(profile.getExperiences().stream().map(ExperienceMapper::toResponse).collect(Collectors.toList()))
                .build();
    }
}
