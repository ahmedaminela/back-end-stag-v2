package com.example.rh.mapper;

import com.example.rh.dto.ApplicationResponse;
import com.example.rh.dto.FileResponse;
import com.example.rh.model.Application;
import com.example.rh.model.File;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationMapper {

    public static ApplicationResponse toApplicationResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .stagiaireId(application.getStagiaire().getId())
                .stagiaireFirstname(application.getStagiaire().getFirstname())
                .stagiaireLastname(application.getStagiaire().getLastname())
                .stageId(application.getStage().getId())
                .submissionDate(application.getSubmissionDate())
                .status(application.getStatus())
                .notes(application.getNotes())
                .files(mapFilesToFileResponses(application.getFiles()))
                .build();
    }

    private static List<FileResponse> mapFilesToFileResponses(List<File> files) {
        return files.stream()
                .map(file -> FileResponse.builder()
                        .id(file.getId())
                        .fileName(file.getFileName())
                        .fileType(file.getFileType())
                        .build())
                .collect(Collectors.toList());
    }
}
