package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="survival_guide")
public class SurvivalGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guideId;

    @Column(length = 3000)
    private String generatedText;
}