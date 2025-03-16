package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.*;
import com.example.FazaaAI.repository.MatchRequestRepository;
import com.example.FazaaAI.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class MatchRequestService {

    @Autowired
    private MatchRequestRepository matchRequestRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationService notificationService;

    public MatchRequest createMatchRequest(Post post1, Post post2, String itemName, int quantity) {
        MatchRequest matchRequest = new MatchRequest();

        // Assign requestPost and offerPost properly
        if (post1.getType().equalsIgnoreCase("request")) {
            matchRequest.setRequestPost(post1);
            matchRequest.setOfferPost(post2);
        } else {
            matchRequest.setRequestPost(post2);
            matchRequest.setOfferPost(post1);
        }

        matchRequest.setItemName(itemName);
        matchRequest.setQuantity(quantity);
        matchRequest.setStatus("pending");

        return matchRequestRepository.save(matchRequest);
    }

    public List<MatchRequest> getUserMatchRequests(Long userId) {
        return matchRequestRepository.findByRequestPostUserIdOrOfferPostUserId(userId, userId);
    }

    @Transactional
    public MatchRequest acceptMatch(Long matchRequestId, Long userId) {

        MatchRequest match = matchRequestRepository.findById(matchRequestId)
                .orElseThrow(() -> new RuntimeException("MatchRequest not found"));

        if (match.getStatus().equalsIgnoreCase("rejected") || match.getStatus().equalsIgnoreCase("completed")) {
            throw new RuntimeException("Cannot accept this match, it's already " + match.getStatus());
        }

        if (match.getRequestPost().getUser().getId().equals(userId)) {
            match.setRequestAccepted(true);
        } else if (match.getOfferPost().getUser().getId().equals(userId)) {
            match.setOfferAccepted(true);
        } else {
            throw new RuntimeException("You are not authorized for this action!");
        }

        // If both accepted: finalize the match
        if (match.isRequestAccepted() && match.isOfferAccepted()) {
            match.setStatus("accepted");

            // Update quantities in posts
            finalizeMatch(match);
        }

        return matchRequestRepository.save(match);
    }

    @Transactional
    public MatchRequest rejectMatch(Long matchRequestId, Long userId) {

        MatchRequest match = matchRequestRepository.findById(matchRequestId)
                .orElseThrow(() -> new RuntimeException("MatchRequest not found"));

        if (!match.getRequestPost().getUser().getId().equals(userId) &&
                !match.getOfferPost().getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized for this action!");
        }

        match.setStatus("rejected");

        // Notify the other user
        String rejectMessage = "Your match for item '" + match.getItemName() + "' has been rejected.";
        if (match.getRequestPost().getUser().getId().equals(userId)) {
            notificationService.createNotification(match.getOfferPost().getUser(), rejectMessage, "reject");
        } else {
            notificationService.createNotification(match.getRequestPost().getUser(), rejectMessage, "reject");
        }

        return matchRequestRepository.save(match);
    }

    @Transactional
    private void finalizeMatch(MatchRequest match) {

        Post requestPost = match.getRequestPost();
        Post offerPost = match.getOfferPost();

        Item requestItem = requestPost.getItems().stream()
                .filter(i -> i.getItemName().equalsIgnoreCase(match.getItemName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Request item not found"));

        Item offerItem = offerPost.getItems().stream()
                .filter(i -> i.getItemName().equalsIgnoreCase(match.getItemName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Offer item not found"));

        int quantityToMatch = match.getQuantity();

        // Subtract quantities
        requestItem.setQuantityRequested(requestItem.getQuantityRequested() - quantityToMatch);
        offerItem.setQuantityAvailable(offerItem.getQuantityAvailable() - quantityToMatch);

        // Mark post status as done if quantities exhausted
        if (requestItem.getQuantityRequested() <= 0) {
            requestPost.setStatus("done");
        }

        if (offerItem.getQuantityAvailable() <= 0) {
            offerPost.setStatus("done");
        }

        postRepository.save(requestPost);
        postRepository.save(offerPost);

        // Notify both users match is completed
        String message = String.format("Match for item '%s' is completed! Quantity matched: %d", match.getItemName(), quantityToMatch);

        notificationService.createNotification(requestPost.getUser(), message, "match-completed");
        notificationService.createNotification(offerPost.getUser(), message, "match-completed");

        // Optional: Mark MatchRequest as completed
        match.setStatus("completed");
        matchRequestRepository.save(match);
    }
}
