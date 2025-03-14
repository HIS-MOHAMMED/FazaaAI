package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.SurvivalGuide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurvivalGuideRepository extends JpaRepository<SurvivalGuide, Long> {
    //SurvivalGuide findByUsername(String username);
}