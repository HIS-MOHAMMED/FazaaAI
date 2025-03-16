package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crisis")
public class Crisis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated primary key

    @Column(nullable = false, columnDefinition = "TEXT")
    private String userDescription;   // What the user wrote (raw input)

    private String Type;        // Extracted from AI

    @Column(columnDefinition = "TEXT")
    private String enhancedDescription; // AI-enhanced description for crisis feed

    private String city;              // Location provided by user

    // Status of crisis

    @Column(columnDefinition = "TEXT") // Store full survival guide text directly in Crisis
    private String survivalGuide;     // AI-generated survival guide text


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getEnhancedDescription() {
        return enhancedDescription;
    }

    public void setEnhancedDescription(String enhancedDescription) {
        this.enhancedDescription = enhancedDescription;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public String getSurvivalGuide() {
        return survivalGuide;
    }

    public void setSurvivalGuide(String survivalGuide) {
        this.survivalGuide = survivalGuide;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}