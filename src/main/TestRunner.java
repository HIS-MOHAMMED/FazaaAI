package com.example.FazaaAI.AIService;

import com.example.FazaaAI.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private AIService aiService;

    @Override
    public void run(String... args) {
        String prompt = "Provide a short survival guide for an earthquake in Cairo.";
        String response = aiService.generateContent(prompt);

        System.out.println("🟢 Llama API Test Response:");
        System.out.println(response);
    }
}
