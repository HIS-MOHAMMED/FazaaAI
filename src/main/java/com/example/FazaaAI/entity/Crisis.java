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

    private String crisisType;        // Extracted from AI

    @Column(columnDefinition = "TEXT")
    private String enhancedDescription; // AI-enhanced description for crisis feed

    private String city;              // Location provided by user


    private boolean resolved = false; // Status of crisis

    @Column(columnDefinition = "TEXT") // Store full survival guide text directly in Crisis
    private String survivalGuide;     // AI-generated survival guide text

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

    public String getCrisisType() {
        return crisisType;
    }

    public void setCrisisType(String crisisType) {
        this.crisisType = crisisType;
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

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getSurvivalGuide() {
        return survivalGuide;
    }

    public void setSurvivalGuide(String survivalGuide) {
        this.survivalGuide = survivalGuide;
    }
}