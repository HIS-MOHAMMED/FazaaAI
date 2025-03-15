package com.example.FazaaAI.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AIService {

    private final String apiKey;
    private final String endpoint;
    private final String modelName;

    public AIService(@Value("${azure.openai.key}") String apiKey,
                     @Value("${azure.openai.endpoint}") String endpoint,
                     @Value("${azure.openai.model}") String modelName) {
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.modelName = modelName;
    }

    public String generateContent(String prompt) {
        String requestUrl = String.format("%s/openai/deployments/%s/chat/completions?api-version=2024-02-01",
                endpoint, modelName);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("api-key", apiKey);
        headers.set("x-ms-model-mesh-model-name", modelName); // REQUIRED clearly for your endpoint

        JSONObject requestBody = new JSONObject();
        JSONArray messagesArray = new JSONArray();
        JSONObject userMessage = new JSONObject();

        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messagesArray.put(userMessage);

        requestBody.put("messages", messagesArray);
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> response = template.exchange(
                    endpoint + "/openai/deployments/" + modelName + "/chat/completions?api-version=2024-02-01",
                    HttpMethod.POST,
                    entity,
                    String.class);

            JSONObject responseJson = new JSONObject(response.getBody());
            return responseJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "AI Service Error: " + e.getMessage();
        }
    }
}