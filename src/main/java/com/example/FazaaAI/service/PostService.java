package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Item;
import com.example.FazaaAI.entity.Post;
import com.example.FazaaAI.repository.PostRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private AIService aiService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MatchRequestService matchRequestService; // new service (we’ll add this later)

    @Transactional
    public Post createPost(Post post) {

        // 1. Use AI to enhance the post
        String prompt = """
        You are an AI assisting in humanitarian aid posts. Analyze this user's post:
        
        "%s"
        
        1. Generate a clear, informative description.
        2. Extract each item with its quantity.
        
        Respond ONLY with raw JSON:
        
        {
          "enhancedDescription": "string",
          "items": [
            { "itemName": "Item Name", "quantity": 5 }
          ]
        }
        """.formatted(post.getUserDescription());

        String aiResponse = aiService.generateContent(prompt);

        // Clean the AI response
        aiResponse = aiResponse.replace("```json", "").replace("```", "").trim();

        JSONObject json = new JSONObject(aiResponse);

        String enhancedDescription = json.getString("enhancedDescription");
        JSONArray itemsArray = json.getJSONArray("items");

        post.setEnhancedDescription(enhancedDescription);

        List<Item> extractedItems = new ArrayList<>();

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemJson = itemsArray.getJSONObject(i);
            Item item = new Item();

            item.setItemName(itemJson.getString("itemName"));

            int quantity = itemJson.getInt("quantity");

            if (post.getType().equalsIgnoreCase("request")) {
                item.setQuantityRequested(quantity);
                item.setQuantityAvailable(0);
            } else {
                item.setQuantityAvailable(quantity);
                item.setQuantityRequested(0);
            }

            item.setPost(post);
            extractedItems.add(item);
        }

        post.setItems(extractedItems);

        // 2. Save the post
        Post savedPost = postRepository.saveAndFlush(post);

        // 3. Trigger matching logic
        checkForMatches(savedPost);

        return savedPost;
    }

    private void checkForMatches(Post post) {

        String oppositeType = post.getType().equalsIgnoreCase("request") ? "offer" : "request";

        List<Post> candidates = postRepository.findByCityAndTypeAndStatus(
                post.getCity(),
                oppositeType,
                "available"
        );

        for (Post candidate : candidates) {

            for (Item item : post.getItems()) {
                for (Item candidateItem : candidate.getItems()) {

                    if (!item.getItemName().equalsIgnoreCase(candidateItem.getItemName())) continue;

                    int postQty = post.getType().equalsIgnoreCase("request") ?
                            item.getQuantityRequested() : item.getQuantityAvailable();

                    int candidateQty = candidate.getType().equalsIgnoreCase("request") ?
                            candidateItem.getQuantityRequested() : candidateItem.getQuantityAvailable();

                    if (postQty <= 0 || candidateQty <= 0) continue;

                    // Found a potential match
                    // ✅ Create MatchRequest (we’ll implement this later)
                    matchRequestService.createMatchRequest(post, candidate, item.getItemName(), Math.min(postQty, candidateQty));

                    // ✅ Send notification to both users
                    String message = String.format("You have a potential match for item '%s'. Please review and accept/reject.", item.getItemName());

                    if (post.getUser() != null) {
                        notificationService.createNotification(post.getUser(), message, "match");
                    }
                    if (candidate.getUser() != null) {
                        notificationService.createNotification(candidate.getUser(), message, "match");
                    }

                    break; // Move to next item/post if one match found
                }
            }
        }
    }

    @Transactional
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post markPostAsDone(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        post.setStatus("done");
        return postRepository.save(post);
    }
}