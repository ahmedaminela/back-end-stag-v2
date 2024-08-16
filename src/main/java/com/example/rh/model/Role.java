package com.example.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String authority;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Permission> authorities = new ArrayList<>();
}
