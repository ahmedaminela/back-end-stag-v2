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
        Profile updatedProfile = profileService.updateProfile(id, request);
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
        profileService.deleteProfile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/{id}/education/add")
    public ResponseEntity<ProfileResponse> addEducation(@PathVariable Long id, @RequestBody EducationRequest educationRequest) {
        Profile updatedProfile = profileService.addEducation(id, educationRequest);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/education/remove/{educationId}")
    public ResponseEntity<ProfileResponse> removeEducation(@PathVariable Long id, @PathVariable Long educationId) {
        Profile updatedProfile = profileService.removeEducation(id, educationId);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/experience/add")
    public ResponseEntity<ProfileResponse> addExperience(@PathVariable Long id, @RequestBody ExperienceRequest experienceRequest) {
        Profile updatedProfile = profileService.addExperience(id, experienceRequest);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/experience/remove/{experienceId}")
    public ResponseEntity<ProfileResponse> removeExperience(@PathVariable Long id, @PathVariable Long experienceId) {
        Profile updatedProfile = profileService.removeExperience(id, experienceId);
        ProfileResponse response = ProfileMapper.toResponse(updatedProfile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
