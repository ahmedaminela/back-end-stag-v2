package com.example.rh.controller;

import com.example.rh.dto.EducationRequest;
import com.example.rh.dto.ExperienceRequest;
import com.example.rh.dto.ProfileRequest;
import com.example.rh.dto.ProfileResponse;
import com.example.rh.mapper.ProfileMapper;
import com.example.rh.model.Profile;
import com.example.rh.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest request) {
        Profile createdProfile = profileService.createProfile(request);
        ProfileResponse response = ProfileMapper.toResponse(createdProfile);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable Long id, @RequestBody ProfileRequest request) {
        Profile updatedProfile = profileService.updateProfileById(id, request);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long id) {
        Profile profile = profileService.getProfileById(id);
        ProfileResponse response = ProfileMapper.toResponse(profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfileById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<ProfileResponse> updateMyProfile(@RequestBody ProfileRequest request) {
        String username = profileService.getCurrentUsername();
        Profile updatedProfile = profileService.updateProfileByUsername(username, request);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile() {
        String username = profileService.getCurrentUsername();
        Profile profile = profileService.getProfileByUsername(username);
        ProfileResponse response = ProfileMapper.toResponse(profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMyProfile() {
        String username = profileService.getCurrentUsername();
        profileService.deleteProfileByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/education/add")
    public ResponseEntity<ProfileResponse> addEducation(@RequestBody EducationRequest educationRequest) {
        String username = profileService.getCurrentUsername();
        Profile updatedProfile = profileService.addEducation(username, educationRequest);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/education/remove/{educationId}")
    public ResponseEntity<ProfileResponse> removeEducation(@PathVariable Long educationId) {
        String username = profileService.getCurrentUsername();
        Profile updatedProfile = profileService.removeEducation(username, educationId);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/experience/add")
    public ResponseEntity<ProfileResponse> addExperience(@RequestBody ExperienceRequest experienceRequest) {
        String username = profileService.getCurrentUsername();
        Profile updatedProfile = profileService.addExperience(username, experienceRequest);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/experience/remove/{experienceId}")
    public ResponseEntity<ProfileResponse> removeExperience(@PathVariable Long experienceId) {
        String username = profileService.getCurrentUsername();
        Profile updatedProfile = profileService.removeExperience(username, experienceId);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
