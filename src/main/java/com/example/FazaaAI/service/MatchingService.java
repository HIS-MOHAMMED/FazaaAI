package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Post;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatchingService {

    public void matchAndNotify(Post post) {
        // Simulated matching logic (later implement database query)
        List<Post> availablePosts = findMatchingPosts(post);

        // Notify matched users (simulate with print statements)
        for (Post matchedPost : availablePosts) {
            System.out.println("🔔 Notification clearly sent to user: " + matchedPost.getContactInfo());
            System.out.println("Matched with post: " + post.getTitle());
        }
    }

    private List<Post> findMatchingPosts(Post post) {
        // Dummy logic (replace clearly with actual database logic later)
        System.out.println("Matching posts clearly based on city: " + post.getCity() + ", urgency: " + post.getUrgency());
        return List.of(); // Later Member 1 adds DB logic
    }
}
