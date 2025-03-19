package com.example.FazaaAI.controller;

import com.example.FazaaAI.dto.NotificationDTO;
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
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {

        List<Notification> notifications = notificationRepository.findByUserId(userId);

        List<NotificationDTO> notificationDTOs = notifications.stream().map(notification -> {
            Long matchRequestId = (notification.getMatchRequest() != null)
                    ? notification.getMatchRequest().getId()
                    : null;

            Long crisisId = (notification.getCrisis() != null)
                    ? notification.getCrisis().getId()
                    : null;

            String crisisType = (notification.getCrisis() != null)
                    ? notification.getCrisis().getType()
                    : null;

            return new NotificationDTO(notification.getId(), notification.getMessage(), notification.getType(), notification.isRead(), matchRequestId, crisisId, crisisType);}).toList();

        return ResponseEntity.ok(notificationDTOs);
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