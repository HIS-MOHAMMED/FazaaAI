package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Item;
import com.example.FazaaAI.entity.Post;
import com.example.FazaaAI.repository.PostRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Post createPost(Post post) {

        // AI PROMPT
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

        System.out.println("✅ AI RAW RESPONSE:\n" + aiResponse);

        // Clean AI Response from markdown artifacts
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

        return postRepository.save(post);
    }

    public void matchRequestsWithOffers() {
        List<Post> requests = postRepository.findByTypeAndStatus("request", "available");
        List<Post> offers = postRepository.findByTypeAndStatus("offer", "available");

        for (Post request : requests) {
            for (Item requestItem : request.getItems()) {

                int neededQuantity = requestItem.getQuantityRequested();
                if (neededQuantity <= 0) continue;

                for (Post offer : offers) {
                    if (!offer.getCity().equalsIgnoreCase(request.getCity())) continue;

                    for (Item offerItem : offer.getItems()) {

                        if (!offerItem.getItemName().equalsIgnoreCase(requestItem.getItemName())) continue;

                        int availableQuantity = offerItem.getQuantityAvailable();
                        if (availableQuantity <= 0) continue;

                        int quantityToMatch = Math.min(neededQuantity, availableQuantity);

                        offerItem.setQuantityAvailable(availableQuantity - quantityToMatch);
                        requestItem.setQuantityRequested(neededQuantity - quantityToMatch);

                        if (offerItem.getQuantityAvailable() == 0) {
                            offer.setStatus("done");
                        }

                        if (requestItem.getQuantityRequested() == 0) {
                            request.setStatus("done");
                        }

                        postRepository.save(offer);
                        postRepository.save(request);

                        notifyUsers(request, offer, quantityToMatch);

                        if (requestItem.getQuantityRequested() == 0) break;
                    }

                    if (requestItem.getQuantityRequested() == 0) break;
                }
            }
        }
    }

    private void notifyUsers(Post request, Post offer, int matchedQuantity) {
        String message = String.format("Your request '%s' matched with an offer '%s'. Matched quantity: %d.",
                request.getEnhancedDescription(),
                offer.getEnhancedDescription(),
                matchedQuantity);

        // Notify request post creator
        if (request.getUser() != null) {
            notificationService.createNotification(request.getUser(), message, "match");
        }

        // Notify offer post creator
        if (offer.getUser() != null) {
            notificationService.createNotification(offer.getUser(), message, "match");
        }
    }

    public Post markPostAsDone(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        post.setStatus("done");
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
