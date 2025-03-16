package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match_request")
public class MatchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who is involved?
    @ManyToOne
    @JoinColumn(name = "request_post_id")
    private Post requestPost;

    @ManyToOne
    @JoinColumn(name = "offer_post_id")
    private Post offerPost;

    private String itemName;
    private int quantity; // Quantity matched

    private boolean requestAccepted = false;
    private boolean offerAccepted = false;

    private String status = "pending"; // "pending", "accepted", "rejected", "completed"

    // Audit info (optional)
    private boolean deleted = false;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getRequestPost() {
        return requestPost;
    }

    public void setRequestPost(Post requestPost) {
        this.requestPost = requestPost;
    }

    public Post getOfferPost() {
        return offerPost;
    }

    public void setOfferPost(Post offerPost) {
        this.offerPost = offerPost;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isRequestAccepted() {
        return requestAccepted;
    }

    public void setRequestAccepted(boolean requestAccepted) {
        this.requestAccepted = requestAccepted;
    }

    public boolean isOfferAccepted() {
        return offerAccepted;
    }

    public void setOfferAccepted(boolean offerAccepted) {
        this.offerAccepted = offerAccepted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}