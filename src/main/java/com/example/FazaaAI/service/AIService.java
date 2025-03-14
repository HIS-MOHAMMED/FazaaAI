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
        // Set HTTP Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(llamaApiKey);

        // Construct JSON request body
        JSONObject requestJson = new JSONObject();
        requestJson.put("model", "llama-13b-chat");
        requestJson.put("messages", new JSONArray()
            .put(new JSONObject().put("role", "user").put("content", prompt))
        );
        requestJson.put("temperature", 0.7);
        requestJson.put("max_tokens", 500);

        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

        try {
            // Call Llama API
            ResponseEntity<String> response = restTemplate.exchange(
                llamaApiUrl,
                HttpMethod.POST,
                entity,
                String.class
            );

            // Parse response
            JSONObject responseBody = new JSONObject(response.getBody());
            return responseBody.getJSONArray("choices")
                               .getJSONObject(0)
                               .getJSONObject("message")
                               .getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating content: " + e.getMessage();
        }
    }
}
