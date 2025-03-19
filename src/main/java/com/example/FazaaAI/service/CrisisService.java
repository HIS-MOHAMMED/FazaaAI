package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.repository.CrisisRepository;
import com.example.FazaaAI.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrisisService {

    @Autowired
    private AIService aiService;

    @Autowired
    private CrisisRepository crisisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public Crisis processCrisisReport(Crisis crisis) {

        String prompt = """
        You are an AI assistant helping classify crisis reports and generate survival guides.
        
        Analyze the following crisis report:
        
        "%s"
        
        ⚠️ Respond ONLY with raw JSON in this format:
        
        {
            "type": "Flood",
            "enhancedDescription": "A massive flood has submerged parts of the city.",
            "survivalGuide": "1. Move to higher ground immediately. 2. Avoid floodwaters. 3. Stay informed via radio...",
            "safetyCheckDurationDays": 3
        }
        """.formatted(crisis.getUserDescription());

        String aiResponse = aiService.generateContent(prompt);
        aiResponse = aiResponse.replace("json", "").replace("```", "").trim();

        JSONObject json = new JSONObject(aiResponse);

        crisis.setType(json.getString("type"));
        crisis.setEnhancedDescription(json.getString("enhancedDescription"));
        crisis.setSurvivalGuide(json.getString("survivalGuide"));

        int durationDays = json.getInt("safetyCheckDurationDays");
        crisis.setSafetyCheckDurationDays(durationDays);

        crisis.setStartDate(LocalDateTime.now());
        crisis.setEndDate(crisis.getStartDate().plusDays(durationDays));

        Crisis savedCrisis = crisisRepository.save(crisis);

        notifyUsersInCity(savedCrisis);

        return savedCrisis;
    }

    private void notifyUsersInCity(Crisis crisis) {
        List<User> usersInCity = userRepository.findByAddressContainingIgnoreCase(crisis.getCity());

        for (User user : usersInCity) {
            String message = String.format("Crisis Alert: %s. Are you safe today?", crisis.getEnhancedDescription());
            notificationService.createSafetyNotification(user, crisis, message);
        }
    }

    public List<Crisis> getAllCrisis() {
        return crisisRepository.findAll();
    }

    public Crisis getCrisisById(Long id) {
        return crisisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crisis not found with ID: " + id));
    }
}