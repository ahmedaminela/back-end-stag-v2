package com.example.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue
    protected Long id;
    protected String username;
    protected String firstname;
    protected String lastname;
    private String password;
    private String phoneNumber;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE")
    private List<Role> authorities = new ArrayList<Role>();
    private String email;
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    //stagiaire
    @ManyToOne
    private User rh;
    @ManyToOne
    private Stage stage;
    @OneToOne
    private Evaluation evaluation;
    @ManyToOne
    private User encadrant;
    @OneToMany(mappedBy = "stagiaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
    //rh
    @OneToMany
    private List<User> stagiairesRh = new ArrayList<>();
    @OneToMany
    private List<Stage> stages = new ArrayList<>();
    //encadrant
    @OneToMany
    private List<User> stagiairesEncadrant = new ArrayList<>();
    @OneToOne
    private Stage stageEncadrant;
}
