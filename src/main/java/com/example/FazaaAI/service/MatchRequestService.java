package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.*;
import com.example.FazaaAI.repository.MatchRequestRepository;
import com.example.FazaaAI.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.example.FazaaAI.entity.Notification;
import java.util.List;

@Service
public class MatchRequestService {

    @Autowired
    private MatchRequestRepository matchRequestRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;


    private MatchRequest matchRequest;




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

        // 1. Retrieve the match request by ID, or throw an exception if not found
        MatchRequest match = matchRequestRepository.findById(matchRequestId)
                .orElseThrow(() -> new RuntimeException("MatchRequest not found with ID: " + matchRequestId));

        // 2. Check if the match has already been rejected or completed
        String status = match.getStatus();
        if ("rejected".equalsIgnoreCase(status) || "completed".equalsIgnoreCase(status)) {
            throw new RuntimeException("Cannot accept this match, it's already " + status);
        }

        // 3. Check if the user is part of this match request
        boolean isRequestUser = match.getRequestPost().getUser().getId().equals(userId);
        boolean isOfferUser = match.getOfferPost().getUser().getId().equals(userId);

        if (!isRequestUser && !isOfferUser) {
            throw new RuntimeException("You are not authorized to accept this match.");
        }
// 4. Mark acceptance from the user
        if (isRequestUser) {
            match.setRequestAccepted(true);
        } else if (isOfferUser) {
            match.setOfferAccepted(true);
        }

        // 5. If both parties have accepted, finalize the match
        if (match.isRequestAccepted() && match.isOfferAccepted()) {

            // Mark as accepted before finalizing
            match.setStatus("accepted");

            // 6. Finalize the match: update item quantities, post statuses, and notify users
            finalizeMatch(match);

            // 7. Reward points to the offer user, based on quantity matched
            int quantityMatched = match.getQuantity();  // this reflects the quantity that was matched
            User offerUser = match.getOfferPost().getUser();

            userService.updateUserReputation(offerUser, quantityMatched);

            // 8. Notify both users of the successful match (optional, if not already handled in finalizeMatch)
            String message = String.format("Match for item '%s' completed! Quantity matched: %d",
                    match.getItemName(), quantityMatched);

            notificationService.createNotification(match.getRequestPost().getUser(), message, "match-completed");
            notificationService.createNotification(offerUser, message, "match-completed");
        }

        // 9. Save the updated match request and return it
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
                .findFirst().orElseThrow(() -> new RuntimeException("Request item not found."));

        Item offerItem = offerPost.getItems().stream()
                .filter(i -> i.getItemName().equalsIgnoreCase(match.getItemName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Offer item not found."));

        int quantityToMatch = match.getQuantity();

        if (quantityToMatch > offerItem.getQuantityAvailable()) {
            throw new RuntimeException("Offer item quantity insufficient.");
        }

        requestItem.setQuantityRequested(requestItem.getQuantityRequested() - quantityToMatch);
        offerItem.setQuantityAvailable(offerItem.getQuantityAvailable() - quantityToMatch);

        if (requestItem.getQuantityRequested() <= 0) {
            requestPost.setStatus("done");
        }

        if (offerItem.getQuantityAvailable() <= 0) {
            offerPost.setStatus("done");
        }

        postRepository.save(requestPost);
        postRepository.save(offerPost);

        match.setStatus("completed");
        matchRequestRepository.save(match);
    }



}
