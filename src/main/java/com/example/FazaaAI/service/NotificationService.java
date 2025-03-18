package com.example.FazaaAI.service;

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


    public void createNotification(User user, String message, String type, MatchRequest matchRequest) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);

        // ✅ Link the MatchRequest
        notification.setMatchRequest(matchRequest);

        notificationRepository.save(notification);

        System.out.println("✅ Notification with MatchRequest saved for user " + user.getId() + ": " + message);
    }
    public void createNotification(User user, String message, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);

        notificationRepository.save(notification);

        System.out.println("✅ Notification saved for user " + user.getId() + ": " + message);
    }

    // New method to notify multiple users
    public void notifyUsers(List<User> users, String message, String type) {
        for (User user : users) {
            createNotification(user, message, type);
        }
    }
}