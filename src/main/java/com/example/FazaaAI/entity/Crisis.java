package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crisis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crisisId;

    private String crisisType;
    private String description;
    private String timeOfCrisis;
    private Boolean resolved;

    private String city;
    private String country;
    private String street;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "guide_id")
    private SurvivalGuide survivalGuide;
}
