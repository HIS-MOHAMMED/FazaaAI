package com.example.FazaaAI.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String type; // "request" or "offer"
    private String status; // "open", "matched", "closed"
    private String urgency; // "low", "medium", "high"
    private String contactInfo;
    private String city;
    private String country;
    private String street;

    private String title;        // AI-generated
    @Column(length = 2000)
    private String description;  // AI-generated

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Item> items;
}