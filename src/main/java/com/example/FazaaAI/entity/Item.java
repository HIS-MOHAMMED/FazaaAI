package com.example.FazaaAI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemName;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
