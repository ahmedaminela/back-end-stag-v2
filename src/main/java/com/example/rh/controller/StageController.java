package com.example.rh.controller;

import com.example.rh.dto.*;
import com.example.rh.enums.StageState;
import com.example.rh.mapper.ApplicationMapper;
import com.example.rh.mapper.StageMapper;
import com.example.rh.mapper.UserMapper;
import com.example.rh.model.Application;
import com.example.rh.model.Stage;
import com.example.rh.service.StageService;
import com.example.rh.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stages")
public class StageController {

    private final StageService stageService;

    @Autowired
    public StageController(StageService stageService) {
        this.stageService = stageService;
    }

    @Operation(summary = "Create a new stage", description = "RH can create a new stage and assign an encadrant to it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stage created successfully", content = @Content(schema = @Schema(implementation = StageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_STAGE')")
    public ResponseEntity<StageResponse> createStage(
            @RequestBody StageCreateRequest request) {
        String currentUsername = stageService.getCurrentUsername();
        Stage createdStage = stageService.createStage(request, currentUsername);
        StageResponse response = StageMapper.toStageResponse(createdStage);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing stage", description = "RH can update the details of an existing stage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stage updated successfully", content = @Content(schema = @Schema(implementation = StageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('UPDATE_STAGE')")
    public ResponseEntity<StageResponse> updateStage(
            @Parameter(description = "ID of the stage to update") @PathVariable Long id,
            @RequestBody Stage updatedStageData) {
        String currentUsername = stageService.getCurrentUsername();
        Stage updatedStage = stageService.updateStage(id, updatedStageData, currentUsername);
        StageResponse response = StageMapper.toStageResponse(updatedStage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a stage", description = "RH can delete a stage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stage deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('DELETE_STAGE')")
    public ResponseEntity<Void> deleteStage(
            @Parameter(description = "ID of the stage to delete") @PathVariable Long id) {
        String currentUsername = stageService.getCurrentUsername();
        stageService.deleteStage(id, currentUsername);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Get details of a stage", description = "Retrieve the details of a specific stage by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stage retrieved successfully", content = @Content(schema = @Schema(implementation = StageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GET_STAGE')")
    public ResponseEntity<StageResponse> getStage(
            @Parameter(description = "ID of the stage to retrieve") @PathVariable Long id) {
        Stage stage = stageService.getStage(id);
        StageResponse response = StageMapper.toStageResponse(stage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all stages", description = "Retrieve a list of all stages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stages retrieved successfully", content = @Content(schema = @Schema(implementation = StageResponse.class)))
    })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('GET_ALL_STAGES')")
    public ResponseEntity<List<StageResponse>> getAllStages() {
        List<Stage> stages = stageService.getAllStages();
        List<StageResponse> responses = stages.stream()
                .map(StageMapper::toStageResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Get stages by state", description = "Retrieve a list of stages filtered by their current state.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stages retrieved successfully", content = @Content(schema = @Schema(implementation = StageResponse.class)))
    })
    @GetMapping("/state/{state}")
    @PreAuthorize("hasAuthority('GET_STAGES_BY_STATE')")
    public ResponseEntity<List<StageResponse>> getStagesByState(
            @Parameter(description = "State to filter stages by") @PathVariable StageState state) {
        List<Stage> stages = stageService.getStagesByState(state);
        List<StageResponse> responses = stages.stream()
                .map(StageMapper::toStageResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Get applications for a stage", description = "Retrieve a list of applications for a specific stage by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @GetMapping("/{stageId}/applications")
    @PreAuthorize("hasAuthority('GET_STAGE')")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForStage(
            @Parameter(description = "ID of the stage to retrieve applications for") @PathVariable Long stageId) {
        List<ApplicationResponse> responses = stageService.getApplicationsForStage(stageId).stream()
                .map(ApplicationMapper::toApplicationResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Stagiaire applies to a stage", description = "Allows a stagiaire to apply for a stage. Creates a notification for the RH.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application submitted successfully", content = @Content(schema = @Schema(implementation = StageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage or Stagiaire not found")
    })
    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('APPLY_FOR_STAGE')")
    public ResponseEntity<StageResponse> applyToStage(
            @RequestBody ApplicationRequest applicationRequest) {

        Stage stage = stageService.applyForStage(applicationRequest);
        StageResponse response = StageMapper.toStageResponse(stage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Accept a stagiaire's application", description = "Allows the RH to accept a stagiaire's application for a stage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application accepted successfully", content = @Content(schema = @Schema(implementation = StageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage or Application not found")
    })
    @PostMapping("/{applicationId}/accept")
    @PreAuthorize("hasAuthority('ACCEPT_APPLICATION')")
    public ResponseEntity<StageResponse> acceptApplication(
            @Parameter(description = "ID of the application to accept") @PathVariable Long applicationId) {
        String currentUsername = stageService.getCurrentUsername();
        Stage updatedStage = stageService.acceptApplication(applicationId, currentUsername);  // No necadrantId needed
        StageResponse response = StageMapper.toStageResponse(updatedStage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Reject a stagiaire's application", description = "Allows the RH to reject a stagiaire's application for a stage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application rejected successfully", content = @Content(schema = @Schema(implementation = StageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage or Application not found")
    })
    @PostMapping("/{applicationId}/reject")
    @PreAuthorize("hasAuthority('REJECT_APPLICATION')")
    public ResponseEntity<StageResponse> rejectApplication(
            @Parameter(description = "ID of the application to reject") @PathVariable Long applicationId) {
        String currentUsername = stageService.getCurrentUsername();
        Stage updatedStage = stageService.rejectApplication(applicationId, currentUsername);
        StageResponse response = StageMapper.toStageResponse(updatedStage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "Cancel a stagiaire's application", description = "Allows a stagiaire to cancel their application for a stage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application canceled successfully", content = @Content(schema = @Schema(implementation = StageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage or Application not found")
    })
    @PostMapping("/{applicationId}/cancel")
    @PreAuthorize("hasAuthority('CANCEL_APPLICATION')")
    public ResponseEntity<StageResponse> cancelApplication(
            @Parameter(description = "ID of the application to cancel") @PathVariable Long applicationId) {
        String currentUsername = stageService.getCurrentUsername();
        Stage updatedStage = stageService.cancelApplication(applicationId, currentUsername);
        StageResponse response = StageMapper.toStageResponse(updatedStage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get stagiaires for a stage", description = "Retrieve a list of stagiaires associated with a specific stage by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stagiaires retrieved successfully", content = @Content(schema = @Schema(implementation = StagiaireResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @GetMapping("/{stageId}/stagiaires")
    @PreAuthorize("hasAuthority('GET_STAGE')")
    public ResponseEntity<List<StagiaireResponse>> getStagiairesForStage(
            @Parameter(description = "ID of the stage to retrieve stagiaires for") @PathVariable Long stageId) {
        Stage stage = stageService.getStage(stageId);
        List<StagiaireResponse> responses = stage.getStagiaires().stream()
                .map(UserMapper::toStagiaireResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Get a user's applications", description = "Retrieve a list of applications submitted by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}/applications")
    @PreAuthorize("hasAuthority('GET_USER_APPLICATIONS')")
    public ResponseEntity<List<ApplicationResponse>> getUserApplications(
            @Parameter(description = "ID of the user to retrieve applications for") @PathVariable Long userId) {
        List<ApplicationResponse> responses = stageService.getUserApplications(userId).stream()
                .map(ApplicationMapper::toApplicationResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Get all stages (paginated)", description = "Retrieve a paginated list of all stages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stages retrieved successfully", content = @Content(schema = @Schema(implementation = StageResponse.class)))
    })
    @GetMapping("/all/paginated")
    @PreAuthorize("hasAuthority('GET_ALL_STAGES')")
    public ResponseEntity<Page<StageResponse>> getAllStagesPaginated(Pageable pageable) {
        Page<Stage> stages = stageService.getAllStagesPaginated(pageable);
        Page<StageResponse> responses = stages.map(StageMapper::toStageResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Get stages by state (paginated)", description = "Retrieve a paginated list of stages filtered by their current state.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stages retrieved successfully", content = @Content(schema = @Schema(implementation = StageResponse.class)))
    })
    @GetMapping("/state/{state}/paginated")
    @PreAuthorize("hasAuthority('GET_STAGES_BY_STATE')")
    public ResponseEntity<Page<StageResponse>> getStagesByStatePaginated(
            @Parameter(description = "State to filter stages by") @PathVariable StageState state, Pageable pageable) {
        Page<Stage> stages = stageService.getStagesByStatePaginated(state, pageable);
        Page<StageResponse> responses = stages.map(StageMapper::toStageResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Search stages", description = "Search for stages by title or description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stages retrieved successfully", content = @Content(schema = @Schema(implementation = StageResponse.class)))
    })
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('GET_ALL_STAGES')")
    public ResponseEntity<List<StageResponse>> searchStages(
            @Parameter(description = "Keyword to search for in title or description") @RequestParam String keyword) {
        List<Stage> stages = stageService.searchStages(keyword);
        List<StageResponse> responses = stages.stream().map(StageMapper::toStageResponse).collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Search stages (paginated)", description = "Search for stages by title or description with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stages retrieved successfully", content = @Content(schema = @Schema(implementation = StageResponse.class)))
    })
    @GetMapping("/search/paginated")
    @PreAuthorize("hasAuthority('GET_ALL_STAGES')")
    public ResponseEntity<Page<StageResponse>> searchStagesPaginated(
            @Parameter(description = "Keyword to search for in title or description") @RequestParam String keyword,
            Pageable pageable) {
        Page<Stage> stages = stageService.searchStagesPaginated(keyword, pageable);
        Page<StageResponse> responses = stages.map(StageMapper::toStageResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Get applications for a stage (paginated)", description = "Retrieve a paginated list of applications for a specific stage by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @GetMapping("/{stageId}/applications/paginated")
    @PreAuthorize("hasAuthority('GET_STAGE')")
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsForStagePaginated(
            @Parameter(description = "ID of the stage to retrieve applications for") @PathVariable Long stageId,
            Pageable pageable) {
        Page<Application> applications = stageService.getApplicationsForStagePaginated(stageId, pageable);
        Page<ApplicationResponse> responses = applications.map(ApplicationMapper::toApplicationResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Search applications by stagiaire name", description = "Search for applications by stagiaire's first name or last name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @GetMapping("/applications/search")
    @PreAuthorize("hasAuthority('GET_ALL_APPLICATIONS')")
    public ResponseEntity<List<ApplicationResponse>> searchApplicationsByStagiaireName(
            @Parameter(description = "Keyword to search for in stagiaire's first or last name") @RequestParam String keyword) {
        List<Application> applications = stageService.searchApplicationsByStagiaireName(keyword);
        List<ApplicationResponse> responses = applications.stream().map(ApplicationMapper::toApplicationResponse).collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Search applications by stagiaire name (paginated)", description = "Search for applications by stagiaire's first name or last name with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @GetMapping("/applications/search/paginated")
    @PreAuthorize("hasAuthority('GET_ALL_APPLICATIONS')")
    public ResponseEntity<Page<ApplicationResponse>> searchApplicationsByStagiaireNamePaginated(
            @Parameter(description = "Keyword to search for in stagiaire's first or last name") @RequestParam String keyword,
            Pageable pageable) {
        Page<Application> applications = stageService.searchApplicationsByStagiaireNamePaginated(keyword, pageable);
        Page<ApplicationResponse> responses = applications.map(ApplicationMapper::toApplicationResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Get a user's applications (paginated)", description = "Retrieve a paginated list of applications submitted by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}/applications/paginated")
    @PreAuthorize("hasAuthority('GET_USER_APPLICATIONS')")
    public ResponseEntity<Page<ApplicationResponse>> getUserApplicationsPaginated(
            @Parameter(description = "ID of the user to retrieve applications for") @PathVariable Long userId,
            Pageable pageable) {
        Page<Application> applications = stageService.getUserApplicationsPaginated(userId, pageable);
        Page<ApplicationResponse> responses = applications.map(ApplicationMapper::toApplicationResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @Operation(summary = "Get my applications", description = "Retrieve a list of applications submitted by the connected user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @GetMapping("/my/applications")
    @PreAuthorize("hasAuthority('GET_MY_APPLICATIONS')")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        List<ApplicationResponse> responses = stageService.getMyApplications().stream()
                .map(ApplicationMapper::toApplicationResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Get my applications (paginated)", description = "Retrieve a paginated list of applications submitted by the connected user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully", content = @Content(schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @GetMapping("/my/applications/paginated")
    @PreAuthorize("hasAuthority('GET_MY_APPLICATIONS')")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplicationsPaginated(Pageable pageable) {
        Page<Application> applications = stageService.getMyApplicationsPaginated(pageable);
        Page<ApplicationResponse> responses = applications.map(ApplicationMapper::toApplicationResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @GetMapping("/encadrant")
    public List<StagiaireResponse> getStagiairesForEncadrant(Authentication authentication) {
        // Assuming the encadrant's ID is stored in the authentication principal
        Long encadrantId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return stageService.getStagiairesByEncadrant(encadrantId);
    }

}

