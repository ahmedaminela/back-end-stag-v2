package com.example.rh.repository;

import com.example.rh.model.Application;
import com.example.rh.model.Stage;
import com.example.rh.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStage(Stage stage);
    Page<Application> findByStage(Stage stage, Pageable pageable);

    List<Application> findByStagiaire(User user);
    Page<Application> findByStagiaire(User user,Pageable pageable);
    List<Application> findByStagiaire_FirstnameContainingOrStagiaire_LastnameContaining(String keyword, String keyword1);
    Page<Application> findByStagiaire_FirstnameContainingOrStagiaire_LastnameContaining(String keyword, String keyword1,Pageable pageable);

}
