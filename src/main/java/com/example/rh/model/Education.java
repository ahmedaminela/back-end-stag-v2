package com.example.rh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institution;
    private String degree;
    private String fieldOfStudy;
    private Date startDate;
    private Date endDate;
    private String description;
    private String location;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
