package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.Post;
import com.example.FazaaAI.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    // Create a new post
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); // Get userId from token
        if (userId != null) {
            post.setId(userId); // Set the userId if present
        }
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    // Manually trigger matching engine
    @PostMapping("/match")
    public ResponseEntity<String> matchPosts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); // Optional: Log who triggered this
        System.out.println("Matching triggered by user: " + (userId != null ? userId : "Anonymous"));
        postService.matchRequestsWithOffers();
        return ResponseEntity.ok("Matching completed!");
    }

    // Mark post as done manually
    @PutMapping("/resolve/{id}")
    public ResponseEntity<Post> resolvePost(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); // Optional: Log who resolved this
        System.out.println("Post resolved by user: " + (userId != null ? userId : "Anonymous"));
        Post updatedPost = postService.markPostAsDone(id);
        return ResponseEntity.ok(updatedPost);
    }

    // Get all posts
    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId"); // Optional: Log who accessed this
        System.out.println("Posts accessed by user: " + (userId != null ? userId : "Anonymous"));
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
}