package com.example.rh.service;

import com.example.rh.dto.EducationRequest;
import com.example.rh.dto.ExperienceRequest;
import com.example.rh.dto.ProfileRequest;
import com.example.rh.model.Profile;
import com.example.rh.model.User;

public interface IProfileService {
    Profile createProfile(ProfileRequest request);

    Profile updateProfileById(Long id, ProfileRequest request);

    void deleteProfileById(Long id);

    Profile updateProfileByUsername(String username, ProfileRequest request);

    Profile getProfileById(Long id);

    void deleteProfileByUsername(String username);

    Profile addEducation(String username, EducationRequest educationRequest);

    Profile removeEducation(String username, Long educationId);

    Profile addExperience(String username, ExperienceRequest experienceRequest);

    Profile removeExperience(String username, Long experienceId);

    Profile getProfileByUsername(String username);

    User getCurrentUser();

    String getCurrentUsername();
}
