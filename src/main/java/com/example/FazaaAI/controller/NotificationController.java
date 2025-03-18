package com.example.FazaaAI.controller;

import com.example.FazaaAI.dto.NotificationDTO;
import com.example.FazaaAI.entity.Notification;
import com.example.FazaaAI.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        List<NotificationDTO> response = notifications.stream().map(n -> {
            Long matchRequestId = n.getMatchRequest() != null ? n.getMatchRequest().getId() : null;
            return new NotificationDTO(
                    n.getId(),
                    n.getMessage(),
                    n.getType(),
                    n.isRead(),
                    matchRequestId
            );
        }).toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found!"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok("Notification marked as read.");
    }
}