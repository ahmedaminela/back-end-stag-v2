package com.example.rh.service;

import com.example.rh.dto.ApplicationRequest;
import com.example.rh.dto.StageCreateRequest;
import com.example.rh.enums.ApplicationStatus;
import com.example.rh.enums.NotificationType;
import com.example.rh.enums.StageState;
import com.example.rh.exception.InvalidOperationException;
import com.example.rh.exception.ResourceNotFoundException;
import com.example.rh.exception.UnauthorizedActionException;
import com.example.rh.model.Application;
import com.example.rh.model.Stage;
import com.example.rh.model.User;
import com.example.rh.repository.ApplicationRepository;
import com.example.rh.repository.StageRepository;
import com.example.rh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StageService {

    private final StageRepository stageRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationService notificationService;

    @Autowired
    public StageService(StageRepository stageRepository, UserRepository userRepository,
                        ApplicationRepository applicationRepository, NotificationService notificationService) {
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.notificationService = notificationService;
    }

    public Stage createStage(StageCreateRequest request, String username) {
        User rh = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("RH not found"));
        User encadrant = userRepository.findById(request.getEncadrantId())
                .orElseThrow(() -> new ResourceNotFoundException("Encadrant not found"));

        Stage stage = new Stage();
        stage.setTitle(request.getTitle());
        stage.setDescription(request.getDescription());
        stage.setStartDate(request.getStartDate());
        stage.setEndDate(request.getEndDate());
        stage.setState(StageState.SCREENING);
        stage.setRh(rh);
        stage.setEncadrant(encadrant);

        stage = stageRepository.save(stage);

        notificationService.createNotification(stage.getEncadrant().getId(),
                "You have been assigned to a new stage with id = " + stage.getId() + ", title = " + stage.getTitle(),
                NotificationType.STAGE_ASSIGNED);
        return stage;
    }

    public Stage updateStage(Long stageId, Stage updatedStageData, String username) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));

        if (!stage.getRh().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Unauthorized: Only the RH who created the stage can update it");
        }

        stage.setTitle(updatedStageData.getTitle());
        stage.setDescription(updatedStageData.getDescription());
        stage.setStartDate(updatedStageData.getStartDate());
        stage.setEndDate(updatedStageData.getEndDate());
        stage.setState(updatedStageData.getState());

        return stageRepository.save(stage);
    }

    public void deleteStage(Long stageId, String username) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));

        if (!stage.getRh().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Unauthorized: Only the RH who created the stage can delete it");
        }
        stageRepository.delete(stage);
    }

    public Stage getStage(Long stageId) {
        return stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
    }

    public List<Stage> getAllStages() {
        return stageRepository.findAll();
    }

    public List<Stage> getStagesByState(StageState state) {
        return stageRepository.findByState(state);
    }

    public Stage applyForStage(ApplicationRequest request) {
        Stage stage = stageRepository.findById(request.getStageId())
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));

        // Find the user (stagiaire) by username
        User stagiaire = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Stagiaire not found"));

        Application application = new Application();
        application.setStage(stage);
        application.setStagiaire(stagiaire);
        application.setSubmissionDate(new Date());
        application.setStatus(ApplicationStatus.PENDING);

        applicationRepository.save(application);

        notificationService.createNotification(stage.getRh().getId(),
                "A new application has been received from " + stagiaire.getFirstname() + " " + stagiaire.getLastname() + " for the stage " + stage.getTitle(),
                NotificationType.APPLICATION_RECEIVED);

        return stage;
    }

    public List<Application> getApplicationsForStage(Long stageId) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        return applicationRepository.findByStage(stage);
    }

    public Stage acceptApplication(Long applicationId, String username) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        Stage stage = application.getStage();

        if (!stage.getRh().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Unauthorized: Only the RH who created the stage can accept applications");
        }
        User stagiaire = application.getStagiaire();

        if (!application.getStatus().equals(ApplicationStatus.PENDING)) {
            throw new InvalidOperationException("Application is not in a pending state");
        }

        application.setStatus(ApplicationStatus.ACCEPTED);
        stage.getStagiaires().add(stagiaire);
        stagiaire.setStage(stage);

        notificationService.createNotification(stagiaire.getId(),
                "Congratulations! You have been accepted for the stage " + stage.getTitle(),
                NotificationType.APPLICATION_ACCEPTED);

        applicationRepository.save(application);
        return stageRepository.save(stage);
    }

    public Stage rejectApplication(Long applicationId, String username) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        Stage stage = application.getStage();

        if (!stage.getRh().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Unauthorized: Only the RH who created the stage can reject applications");
        }

        if (!application.getStatus().equals(ApplicationStatus.PENDING)) {
            throw new InvalidOperationException("Application is not in a pending state");
        }

        application.setStatus(ApplicationStatus.REJECTED);

        notificationService.createNotification(application.getStagiaire().getId(),
                "We regret to inform you that your application for the stage " + stage.getTitle() + " has been rejected.",
                NotificationType.APPLICATION_REJECTED);

        applicationRepository.save(application);
        return stageRepository.save(stage);
    }

    public Stage cancelApplication(Long applicationId, String username) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getStagiaire().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Unauthorized: Only the stagiaire who created the application can cancel it");
        }
        if (!application.getStatus().equals(ApplicationStatus.PENDING)) {
            throw new InvalidOperationException("Only pending applications can be canceled");
        }

        application.setStatus(ApplicationStatus.CANCELED);

        notificationService.createNotification(application.getStage().getRh().getId(),
                application.getStagiaire().getFirstname() + " " + application.getStagiaire().getLastname() + " has canceled their application for the stage " + application.getStage().getTitle(),
                NotificationType.APPLICATION_CANCELED);

        return application.getStage();
    }

    public List<Application> getUserApplications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return applicationRepository.findByStagiaire(user);
    }

    public Page<Stage> getAllStagesPaginated(Pageable pageable) {
        return stageRepository.findAll(pageable);
    }

    public Page<Stage> getStagesByStatePaginated(StageState state, Pageable pageable) {
        return stageRepository.findByState(state, pageable);
    }

    public List<Stage> searchStages(String keyword) {
        return stageRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
    }

    public Page<Stage> searchStagesPaginated(String keyword, Pageable pageable) {
        return stageRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword, pageable);
    }

    public Page<Application> getApplicationsForStagePaginated(Long stageId, Pageable pageable) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        return applicationRepository.findByStage(stage, pageable);
    }

    public List<Application> searchApplicationsByStagiaireName(String keyword) {
        return applicationRepository.findByStagiaire_FirstnameContainingOrStagiaire_LastnameContaining(keyword, keyword);
    }

    public Page<Application> searchApplicationsByStagiaireNamePaginated(String keyword, Pageable pageable) {
        return applicationRepository.findByStagiaire_FirstnameContainingOrStagiaire_LastnameContaining(keyword, keyword, pageable);
    }

    public Page<Application> getUserApplicationsPaginated(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return applicationRepository.findByStagiaire(user, pageable);
    }

    public List<Application> getMyApplications() {
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return applicationRepository.findByStagiaire(user);
    }

    public Page<Application> getMyApplicationsPaginated(Pageable pageable) {
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return applicationRepository.findByStagiaire(user, pageable);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
