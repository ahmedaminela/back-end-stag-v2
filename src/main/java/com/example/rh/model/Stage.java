package com.example.rh.model;


import com.example.rh.enums.StageState;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Stage{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<User> stagiaires = new ArrayList<>();
    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
    @ManyToOne
    private User encadrant;
    private String title;
    private Date startDate;
    private Date endDate;
    private String description;
    private StageState state;
    @ManyToOne
    private User rh;
}