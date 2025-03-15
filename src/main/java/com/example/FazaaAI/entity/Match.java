package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    private String status; // "pending", "accepted", "rejected"
    private Integer requestedQuantity;
    private Integer matchedQuantity;

    @ManyToOne
    @JoinColumn(name = "requested_post_id")
    private Post requestedPost;

    @ManyToOne
    @JoinColumn(name = "offered_post_id")
    private Post offeredPost;

}
