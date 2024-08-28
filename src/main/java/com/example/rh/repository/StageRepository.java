package com.example.rh.repository;

import com.example.rh.enums.StageState;
import com.example.rh.model.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {
    List<Stage> findByState(StageState stageState);
    Page<Stage> findByState(StageState stageState,Pageable pageable);

    List<Stage> findByTitleContainingOrDescriptionContaining(String keyword, String keyword1);
    Page<Stage> findByTitleContainingOrDescriptionContaining(String keyword, String keyword1, Pageable pageable);
}
