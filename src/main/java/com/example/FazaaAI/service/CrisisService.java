package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.repository.CrisisRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrisisService {

    @Autowired
    private CrisisRepository crisisRepository;

    @Autowired
    private AIService aiService;

    // ✅ Create crisis with AI classification and guide
    public Crisis createCrisis(Crisis crisis) {

        // AI prompt to classify and generate survival guide
        String prompt = """
        You are an AI assistant helping classify crisis reports and generate survival guides.
        
        Analyze the following crisis report:
        
        "%s"
        
        Respond ONLY with raw JSON:
        
        {
          "type": "Flood",
          "enhancedDescription": "A massive flood has submerged parts of the city.",
          "survivalGuide": "1. Move to higher ground immediately. 2. Avoid floodwaters. 3. Stay informed via radio..."
        }
        """.formatted(crisis.getUserDescription());

        String aiResponse = aiService.generateContent(prompt);

        // Clean response if needed
        aiResponse = aiResponse.replace("```json", "").replace("```", "").trim();

        JSONObject json = new JSONObject(aiResponse);

        crisis.setType(json.getString("type"));
        crisis.setEnhancedDescription(json.getString("enhancedDescription"));
        crisis.setSurvivalGuide(json.getString("survivalGuide"));



        return crisisRepository.save(crisis);
    }

    // ✅ Get all crisis posts
    public List<Crisis> getAllCrisis() {
        return crisisRepository.findAll();
    }

    // ✅ Get specific crisis post by ID
    public Crisis getCrisisById(Long id) {
        return crisisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crisis not found with ID: " + id));
    }

    // ✅ Update crisis status (optional)
    public Crisis updateCrisisStatus(Long id, String status) {
        Crisis crisis = getCrisisById(id);
        return crisisRepository.save(crisis);
    }
}
