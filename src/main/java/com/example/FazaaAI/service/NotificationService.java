package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.MatchRequest;
import com.example.FazaaAI.entity.Notification;
import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Create a generic notification with match request reference (used for matches)
    public void createNotification(User user, String message, String type, MatchRequest matchRequest) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setMatchRequest(matchRequest);

        notificationRepository.save(notification);
    }

    // Create a generic notification without match request
    public void createNotification(User user, String message, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    // ✅ SAFETY NOTIFICATION with safetyStatus set to 'pending'
    public void createSafetyNotification(User user, Crisis crisis, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType("safety_check"); // Convention: use underscore for type
        notification.setRead(false);

        // THIS WAS MISSING!
        notification.setSafetyStatus("pending"); // Start with "pending"

        notificationRepository.save(notification);

        System.out.println("✅ Sent safety notification to user " + user.getUsername());
    }

    // Notify a list of users (general purpose)
    public void notifyUsers(List<User> users, String message, String type) {
        for (User user : users) {
            createNotification(user, message, type);
        }
    }
}