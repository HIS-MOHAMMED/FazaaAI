package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserDescription() { return userDescription; }
    public void setUserDescription(String userDescription) { this.userDescription = userDescription; }
    public String getEnhancedDescription() { return enhancedDescription; }
    public void setEnhancedDescription(String enhancedDescription) { this.enhancedDescription = enhancedDescription; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getSurvivalGuide() { return survivalGuide; }
    public void setSurvivalGuide(String survivalGuide) { this.survivalGuide = survivalGuide; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getType() { return Type; }
    public void setType(String type) { Type = type; }
}