package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.Notification;
import com.example.FazaaAI.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found!"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok("Notification marked as read.");
    }

    // NEW: Respond to Safety Check
    @PutMapping("/safety-response/{notificationId}")
    public ResponseEntity<?> updateSafetyStatus(
            @PathVariable Long notificationId,
            @RequestParam String status,
            HttpServletRequest request
    ) {
        Object userIdObj = request.getAttribute("userId");
        System.out.println("✅ userId attribute: " + userIdObj);

        if (userIdObj == null) {
            return ResponseEntity.status(403).body("Unauthorized - No userId in request");
        }

        Long userId = (Long) userIdObj;

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).body("You can't update this notification");
        }

        notification.setSafetyStatus(status);
        notification.setRead(true);
        notificationRepository.save(notification);

        return ResponseEntity.ok("Status updated to: " + status);
    }
}