package com.example.FazaaAI.controller;

import com.example.FazaaAI.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateContent(@RequestParam String prompt) {
        String response = aiService.generateContent(prompt);
        return ResponseEntity.ok(response);
    }
}