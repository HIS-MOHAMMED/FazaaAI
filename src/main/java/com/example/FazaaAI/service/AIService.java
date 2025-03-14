package com.example.FazaaAI.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIService {

    @Value("${llama.api.key}")
    private String llamaApiKey;

    private final String llamaApiUrl = "https://api.llama-api.com/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateContent(String prompt) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(llamaApiKey);

        JSONObject requestJson = new JSONObject()
            .put("model", "llama3.1-70b") // Clearly choose your preferred model
            .put("messages", new JSONArray()
                .put(new JSONObject()
                    .put("role", "user")
                    .put("content", prompt)
                )
            )
            .put("temperature", 0.1)
            .put("max_tokens", 500)
            .put("top_p", 1.0)
            .put("frequency_penalty", 1.0);

        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.llama-api.com/chat/completions",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JSONObject responseBody = new JSONObject(response.getBody());

            return responseBody.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "AI Service Error: " + e.getMessage();
        }
    }
}
