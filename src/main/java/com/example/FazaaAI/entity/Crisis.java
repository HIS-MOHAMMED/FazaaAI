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
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String userDescription;

    private String Type;

    @Column(columnDefinition = "TEXT")
    private String enhancedDescription;

    private String city;

    @Column(columnDefinition = "TEXT")
    private String survivalGuide;

    private int safetyCheckDurationDays; // AI suggestion of days

    private LocalDateTime startDate;  // When crisis was created

    private LocalDateTime endDate;    // startDate + safetyCheckDurationDays

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.startDate = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getSafetyCheckDurationDays() {
        return safetyCheckDurationDays;
    }

    public void setSafetyCheckDurationDays(int safetyCheckDurationDays) {
        this.safetyCheckDurationDays = safetyCheckDurationDays;
    }

    public String getSurvivalGuide() {
        return survivalGuide;
    }

    public void setSurvivalGuide(String survivalGuide) {
        this.survivalGuide = survivalGuide;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEnhancedDescription() {
        return enhancedDescription;
    }

    public void setEnhancedDescription(String enhancedDescription) {
        this.enhancedDescription = enhancedDescription;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}