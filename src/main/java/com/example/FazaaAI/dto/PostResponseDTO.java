package com.example.FazaaAI.dto;

import lombok.*;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String userDescription;
    private String enhancedDescription;
    private String status;
    private String urgency;
    private String type;
    private String city;
    private String phoneNumber;

    private Long userId;      // This is what mobile needs
    private String username;  // Optional, but useful for display

    public PostResponseDTO(Long id, String userDescription, String enhancedDescription, String status, String urgency, String type, String city, String phoneNumber, Long userId, String username) {
        this.id = id;
        this.userDescription = userDescription;
        this.enhancedDescription = enhancedDescription;
        this.status = status;
        this.urgency = urgency;
        this.type = type;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.username = username;
    }

    public PostResponseDTO( ) {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}