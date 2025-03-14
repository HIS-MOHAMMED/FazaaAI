package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private AIService aiService;

    public void generatePostContent(Post post, String userDescription) {
        String prompt = "Convert this user's message into a clear, structured emergency post: " 
                        + userDescription + ". Include a short title and clear description.";

        String aiResponse = aiService.generateContent(prompt);

        String[] parts = aiResponse.split("\n", 2);
        if (parts.length >= 2) {
            post.setTitle(parts[0].trim());
            post.setDescription(parts[1].trim());
        } else {
            post.setTitle("Urgent Request");
            post.setDescription(aiResponse.trim());
        }
    }
}
