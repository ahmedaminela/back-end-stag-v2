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
    private String telephone;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE")
    private List<Role> authorities = new ArrayList<Role>();
    private String email;
}
