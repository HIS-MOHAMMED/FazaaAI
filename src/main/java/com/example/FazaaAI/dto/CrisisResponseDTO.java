package com.example.FazaaAI.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class CrisisResponseDTO {
    public CrisisResponseDTO(String username, Long userId, LocalDateTime endDate, LocalDateTime startDate, int safetyCheckDurationDays, String survivalGuide, String city, String type, String enhancedDescription, String userDescription, Long id) {
        this.username = username;//
        this.userId = userId;//
        this.endDate = endDate;//
        this.startDate = startDate;//
        this.safetyCheckDurationDays = safetyCheckDurationDays;//
        this.survivalGuide = survivalGuide;//
        this.city = city;//
        this.type = type;
        this.enhancedDescription = enhancedDescription;//
        this.userDescription = userDescription;//
        this.id = id;//
    }

    private Long id;
    private String userDescription;
    private String enhancedDescription;
    private String type;
    private String city;
    private String survivalGuide;

    private int safetyCheckDurationDays;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long userId;        // ID of the user who created it
    private String username;    // Username of the creator
    public CrisisResponseDTO(Long id, String userDescription, String enhancedDescription, String city,
                             String survivalGuide, String type, int safetyCheckDurationDays,
                             LocalDateTime startDate, LocalDateTime endDate,
                             Long userId, String username) {
        this.id = id;
        this.userDescription = userDescription;
        this.enhancedDescription = enhancedDescription;
        this.city = city;
        this.survivalGuide = survivalGuide;
        this.type = type;
        this.safetyCheckDurationDays = safetyCheckDurationDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.username = username;
    }



    public String getSurvivalGuide() {
        return survivalGuide;
    }

    public void setSurvivalGuide(String survivalGuide) {
        this.survivalGuide = survivalGuide;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSafetyCheckDurationDays() {
        return safetyCheckDurationDays;
    }

    public void setSafetyCheckDurationDays(int safetyCheckDurationDays) {
        this.safetyCheckDurationDays = safetyCheckDurationDays;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}