package com.example.rh.service;

import com.example.rh.dto.EducationRequest;
import com.example.rh.dto.ExperienceRequest;
import com.example.rh.dto.ProfileRequest;
import com.example.rh.mapper.EducationMapper;
import com.example.rh.mapper.ExperienceMapper;
import com.example.rh.mapper.ProfileMapper;
import com.example.rh.model.Education;
import com.example.rh.model.Experience;
import com.example.rh.model.Profile;
import com.example.rh.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile createProfile(ProfileRequest request) {
        Profile profile = ProfileMapper.toEntity(request);
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long id, ProfileRequest request) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isEmpty()) {
            throw new RuntimeException("Profile not found");
        }
        Profile existingProfile = optionalProfile.get();
        existingProfile.setFirstName(request.getFirstName());
        existingProfile.setLastName(request.getLastName());
        existingProfile.setPhoneNumber(request.getPhoneNumber());
        existingProfile.setEmail(request.getEmail());
        existingProfile.setProfilePicture(request.getProfilePicture());
        existingProfile.setEducations(ProfileMapper.toEntity(request).getEducations());
        existingProfile.setExperiences(ProfileMapper.toEntity(request).getExperiences());
        return profileRepository.save(existingProfile);
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found");
        }
        profileRepository.deleteById(id);
    }
    public Profile addEducation(Long profileId, EducationRequest educationRequest) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Education education = EducationMapper.toEntity(educationRequest);
        profile.getEducations().add(education);
        education.setProfile(profile); // Assuming Education entity has a reference to Profile

        return profileRepository.save(profile);
    }

    public Profile removeEducation(Long profileId, Long educationId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Education educationToRemove = profile.getEducations().stream()
                .filter(education -> education.getId().equals(educationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Education not found"));

        profile.getEducations().remove(educationToRemove);

        return profileRepository.save(profile);
    }

    public Profile addExperience(Long profileId, ExperienceRequest experienceRequest) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Experience experience = ExperienceMapper.toEntity(experienceRequest);
        profile.getExperiences().add(experience);
        experience.setProfile(profile); // Assuming Experience entity has a reference to Profile

        return profileRepository.save(profile);
    }

    public Profile removeExperience(Long profileId, Long experienceId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Experience experienceToRemove = profile.getExperiences().stream()
                .filter(experience -> experience.getId().equals(experienceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        profile.getExperiences().remove(experienceToRemove);

        return profileRepository.save(profile);
    }
}
