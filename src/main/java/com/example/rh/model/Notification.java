package com.example.rh.model;

import com.example.rh.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User recipient;

    private String message;
    @Column(name = "is_read")
    private boolean read;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;


    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
