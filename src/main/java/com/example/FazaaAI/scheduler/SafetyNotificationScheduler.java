package com.example.FazaaAI.scheduler;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.repository.CrisisRepository;
import com.example.FazaaAI.repository.UserRepository;
import com.example.FazaaAI.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SafetyNotificationScheduler {

    @Autowired
    private CrisisRepository crisisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // Runs every day at 9 AM (use * for testing per minute)
    @Scheduled(cron = "0 0 9 * * *") // every day at 9 AM
    public void sendDailySafetyNotifications() {

        LocalDateTime now = LocalDateTime.now();
        List<Crisis> activeCrisisList = crisisRepository.findAll();

        for (Crisis crisis : activeCrisisList) {

            LocalDateTime startDate = crisis.getStartDate();
            LocalDateTime endDate = crisis.getEndDate();

            if (startDate == null || endDate == null) {
                continue; // invalid record
            }

            if (now.isBefore(startDate) || now.isAfter(endDate)) {
                continue; // skip if not within range
            }

            List<User> usersInCity = userRepository.findByAddressContainingIgnoreCase(crisis.getCity());

            for (User user : usersInCity) {
                String message = String.format("Daily Safety Check: Crisis '%s' is still ongoing. Are you safe today?", crisis.getType());
                notificationService.createSafetyNotification(user, crisis, message);
            }
        }
    }
}