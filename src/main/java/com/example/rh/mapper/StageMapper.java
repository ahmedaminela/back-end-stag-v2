package com.example.rh.mapper;

import com.example.rh.dto.ApplicationResponse;
import com.example.rh.dto.StageResponse;
import com.example.rh.dto.StagiaireResponse;
import com.example.rh.model.Application;
import com.example.rh.model.Stage;
import com.example.rh.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class StageMapper {
    public static StageResponse toStageResponse(Stage stage) {
        return StageResponse.builder()
                .id(stage.getId())
                .title(stage.getTitle())
                .startDate(stage.getStartDate())
                .endDate(stage.getEndDate())
                .description(stage.getDescription())
                .state(stage.getState())
                .rh(UserMapper.toRhResponse(stage.getRh()))
                .encadrant(stage.getEncadrant() != null ? UserMapper.toEncadrantResponse(stage.getEncadrant()) : null)
                .applications(mapApplicationsToApplicationResponses(stage.getApplications()))
                .stagiaires(mapUsersToStagiaireResponses(stage.getStagiaires()))
                .build();
    }

    private static List<ApplicationResponse> mapApplicationsToApplicationResponses(List<Application> applications) {
        return applications.stream()
                .map(ApplicationMapper::toApplicationResponse)
                .collect(Collectors.toList());
    }

    private static List<StagiaireResponse> mapUsersToStagiaireResponses(List<User> users) {
        return users.stream()
                .map(UserMapper::toStagiaireResponse)
                .collect(Collectors.toList());
    }
}
