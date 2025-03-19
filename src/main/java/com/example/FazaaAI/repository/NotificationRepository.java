package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByCrisisId(Long crisisId); // NEW: for safety campaign follow-up
}