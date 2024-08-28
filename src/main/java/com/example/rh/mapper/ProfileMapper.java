package com.example.rh.mapper;

import com.example.rh.dto.ProfileRequest;
import com.example.rh.dto.ProfileResponse;
import com.example.rh.model.Profile;

import java.util.stream.Collectors;

public class ProfileMapper {

    public static Profile toEntity(ProfileRequest request) {
        return Profile.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .profilePicture(request.getProfilePicture())
                .educations(request.getEducations().stream().map(EducationMapper::toEntity).collect(Collectors.toList()))
                .experiences(request.getExperiences().stream().map(ExperienceMapper::toEntity).collect(Collectors.toList()))
                .build();
    }

    public static ProfileResponse toResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .email(profile.getEmail())
                .profilePicture(profile.getProfilePicture())
                .educations(profile.getEducations().stream().map(EducationMapper::toResponse).collect(Collectors.toList()))
                .experiences(profile.getExperiences().stream().map(ExperienceMapper::toResponse).collect(Collectors.toList()))
                .build();
    }
}
