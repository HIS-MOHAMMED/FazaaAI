package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.SurvivalGuide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrisisService {

    @Autowired
    private AIService aiService;

    public SurvivalGuide generateSurvivalGuide(Crisis crisis) {
        String prompt = "Provide clear and concise step-by-step survival instructions for a " 
                        + crisis.getCrisisType() + " happening in "
                        + crisis.getCity() + ".";

        String guideText = aiService.generateContent(prompt);

        SurvivalGuide guide = new SurvivalGuide();
        guide.setGeneratedText(guideText);
        return guide;
    }
}
