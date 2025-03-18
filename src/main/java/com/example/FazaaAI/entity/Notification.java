package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference; // Changed to Back

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-notifications") // Back-reference to the user
    private User user;

    @ManyToOne
    @JoinColumn(name = "match_request_id")
    private MatchRequest matchRequest;
    // Getters and setters remain unchanged

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