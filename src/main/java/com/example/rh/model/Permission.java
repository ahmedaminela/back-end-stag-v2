package com.example.rh.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String authority;
}
