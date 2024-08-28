package com.example.rh.service;

import com.example.rh.dto.EducationRequest;
import com.example.rh.dto.ExperienceRequest;
import com.example.rh.dto.ProfileRequest;
import com.example.rh.exception.ResourceNotFoundException;
import com.example.rh.mapper.EducationMapper;
import com.example.rh.mapper.ExperienceMapper;
import com.example.rh.mapper.ProfileMapper;
import com.example.rh.model.Education;
import com.example.rh.model.Experience;
import com.example.rh.model.Profile;
import com.example.rh.model.User;
import com.example.rh.repository.ProfileRepository;
import com.example.rh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements IProfileService{

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Profile createProfile(ProfileRequest request) {
        User currentUser = getCurrentUser();
        Profile profile = ProfileMapper.toEntity(request);
        profile.setUser(currentUser);
        return profileRepository.save(profile);
    }
    @Override
    public Profile updateProfileById(Long id, ProfileRequest request) {
        Profile existingProfile = getProfileById(id);
        existingProfile.setProfilePicture(request.getProfilePicture());
        existingProfile.setEducations(ProfileMapper.toEntity(request).getEducations());
        existingProfile.setExperiences(ProfileMapper.toEntity(request).getExperiences());
        return profileRepository.save(existingProfile);
    }
    @Override
    public void deleteProfileById(Long id) {
        profileRepository.deleteById(id);
    }
    @Override
    public Profile updateProfileByUsername(String username, ProfileRequest request) {
        Profile existingProfile = getProfileByUsername(username);
        existingProfile.setProfilePicture(request.getProfilePicture());
        existingProfile.setEducations(ProfileMapper.toEntity(request).getEducations());
        existingProfile.setExperiences(ProfileMapper.toEntity(request).getExperiences());
        return profileRepository.save(existingProfile);
    }
    @Override
    public Profile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
    @Override
    public void deleteProfileByUsername(String username) {
        Profile profile = getProfileByUsername(username);
        profileRepository.delete(profile);
    }
    @Override
    public Profile addEducation(String username, EducationRequest educationRequest) {
        Profile profile = getProfileByUsername(username);

        Education education = EducationMapper.toEntity(educationRequest);
        profile.getEducations().add(education);
        education.setProfile(profile);

        return profileRepository.save(profile);
    }
    @Override
    public Profile removeEducation(String username, Long educationId) {
        Profile profile = getProfileByUsername(username);

        Education educationToRemove = profile.getEducations().stream()
                .filter(education -> education.getId().equals(educationId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));

        profile.getEducations().remove(educationToRemove);

        return profileRepository.save(profile);
    }
    @Override
    public Profile addExperience(String username, ExperienceRequest experienceRequest) {
        Profile profile = getProfileByUsername(username);

        Experience experience = ExperienceMapper.toEntity(experienceRequest);
        profile.getExperiences().add(experience);
        experience.setProfile(profile);

        return profileRepository.save(profile);
    }
    @Override
    public Profile removeExperience(String username, Long experienceId) {
        Profile profile = getProfileByUsername(username);

        Experience experienceToRemove = profile.getExperiences().stream()
                .filter(experience -> experience.getId().equals(experienceId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));

        profile.getExperiences().remove(experienceToRemove);

        return profileRepository.save(profile);
    }
    @Override
    public Profile getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
    @Override
    public User getCurrentUser() {
        return userRepository.findByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
