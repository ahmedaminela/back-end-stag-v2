package com.example.rh.mapper;

import com.example.rh.dto.EncadrantResponse;
import com.example.rh.dto.RhResponse;
import com.example.rh.dto.StagiaireResponse;
import com.example.rh.model.User;

public class UserMapper {

    public static StagiaireResponse toStagiaireResponse(User stagiaire) {
        return StagiaireResponse.builder()
                .id(stagiaire.getId())
                .firstname(stagiaire.getFirstname())
                .lastname(stagiaire.getLastname())
                .email(stagiaire.getEmail())
                .phoneNumber(stagiaire.getPhoneNumber())
                .rhName(stagiaire.getRh() != null ? stagiaire.getRh().getFirstname() + " " + stagiaire.getRh().getLastname() : null)
                .stageTitle(stagiaire.getStage() != null ? stagiaire.getStage().getTitle() : null)
                .encadrantName(stagiaire.getEncadrant() != null ? stagiaire.getEncadrant().getFirstname() + " " + stagiaire.getEncadrant().getLastname() : null)
                .startDate(stagiaire.getStage() != null ? stagiaire.getStage().getStartDate() : null)  // Add startDate
                .endDate(stagiaire.getStage() != null ? stagiaire.getStage().getEndDate() : null)      // Add endDate
                .build();
    }

    public static RhResponse toRhResponse(User user) {
        return RhResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static EncadrantResponse toEncadrantResponse(User user) {
        return EncadrantResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .stageId(user.getStageEncadrant() != null ? user.getStageEncadrant().getId() : null)
                .phoneNumber(user.getPhoneNumber())
                .build();
}}
