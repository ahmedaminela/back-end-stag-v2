package com.example.rh.mapper;

import com.example.rh.dto.EncadrantResponse;
import com.example.rh.dto.RhResponse;
import com.example.rh.dto.StagiaireResponse;
import com.example.rh.model.User;

public class UserMapper {

    public static StagiaireResponse toStagiaireResponse(User user) {
        return StagiaireResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .rhId(user.getRh() != null ? user.getRh().getId() : null)
                .stageId(user.getStage() != null ? user.getStage().getId() : null)
                .encadrantId(user.getEncadrant() != null ? user.getEncadrant().getId() : null)
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
