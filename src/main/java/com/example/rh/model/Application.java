package com.example.rh.model;

import com.example.rh.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stagiaire_id")
    private User stagiaire;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private Double notes;
    private String encadrantComments;  // Comments given by encadrant

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();
}
