package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //SurvivalGuide findByUsername(String username);
}
