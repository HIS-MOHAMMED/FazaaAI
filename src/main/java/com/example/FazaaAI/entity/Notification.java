package com.example.FazaaAI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String type;
    private boolean isRead = false;

    private String safetyStatus; // "pending", "safe", "not_safe"

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-notifications")
    private User user;

    @ManyToOne
    @JoinColumn(name = "match_request_id")
    private MatchRequest matchRequest;

    // NEW: Link this notification to a crisis (for safety campaign)
    @ManyToOne
    @JoinColumn(name = "crisis_id")
    //@JsonIgnore
    private Crisis crisis;

    // Getters and setters remain unchanged


    public String getSafetyStatus() {
        return safetyStatus;
    }

    public void setSafetyStatus(String safetyStatus) {
        this.safetyStatus = safetyStatus;
    }

    public Crisis getCrisis() {
        return crisis;
    }

    public void setCrisis(Crisis crisis) {
        this.crisis = crisis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MatchRequest getMatchRequest() {
        return matchRequest;
    }

    public void setMatchRequest(MatchRequest matchRequest) {
        this.matchRequest = matchRequest;
    }
}