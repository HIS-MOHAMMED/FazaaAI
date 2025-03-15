package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.repository.CrisisRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrisisService {

    @Autowired
    private AIService aiService;

    @Autowired
    private CrisisRepository crisisRepository;
    // Process user input and populate crisis data including survival guide
    public Crisis processCrisisReport(Crisis crisis) {
        String prompt = """
You are a crisis management AI. Respond only in raw JSON (no markdown, no explanations, no pre-text). A user submitted this description:

"%s"

Write a short, clear enhanced description suitable for a public crisis alert feed.
Identify the type of crisis (one of: Earthquake, Flood, War, etc.).
Generate a detailed step-by-step survival guide for people affected.

⚠️ Respond ONLY with raw JSON in this format:

{
  "enhancedDescription": "string",
  "crisisType": "string",
  "survivalGuide": "string"
}
""".formatted(crisis.getUserDescription());


        String aiResponse = aiService.generateContent(prompt);

        JSONObject json = new JSONObject(aiResponse);

        crisis.setEnhancedDescription(json.getString("enhancedDescription"));
        crisis.setCrisisType(json.getString("crisisType"));
        crisis.setSurvivalGuide(json.getString("survivalGuide"));

        // Simulate DB save
        Crisis savedCrisis = crisisRepository.save(crisis);
        System.out.println("✅ Crisis processed and ready to save: " + crisis);

        notifyAreaUsers(crisis);

        return crisis;
    }

    public void notifyAreaUsers(Crisis crisis) {
        System.out.println("🔔 New Crisis Alert in " + crisis.getCity() + "!");
        System.out.println("📝 Enhanced Description: " + crisis.getEnhancedDescription());
        System.out.println("📜 Survival Guide: " + crisis.getSurvivalGuide());
    }

    public Crisis resolveCrisis(Long crisisId) {
        // ✅ Fetch Crisis from Database
        Crisis crisis = crisisRepository.findById(crisisId)
                .orElseThrow(() -> new RuntimeException("Crisis not found with ID: " + crisisId));

        // ✅ Mark as resolved
        crisis.setResolved(true);

        // ✅ Save the updated crisis in the database
        Crisis updatedCrisis = crisisRepository.save(crisis);

        System.out.println("✅ Crisis marked as resolved and saved: " + updatedCrisis);

        return updatedCrisis;
    }
}
