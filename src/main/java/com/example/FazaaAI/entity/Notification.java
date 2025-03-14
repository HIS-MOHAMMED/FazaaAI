package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String title;
    @Column(length = 1000)
    private String message;
    private Boolean isRead = false;
    private String sentAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}