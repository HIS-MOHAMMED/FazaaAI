package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.Post;
import com.example.FazaaAI.service.PostService;
import com.example.FazaaAI.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MatchingService matchingService;

    @PostMapping("/{type}")
    public ResponseEntity<Post> createPost(@PathVariable String type, @RequestBody Post post) {
        post.setType(type);
        postService.generatePostContent(post, post.getDescription());
        // Save to DB (Member 1 will handle saving clearly)
        matchingService.matchAndNotify(post); // clearly handle matching & notifications
        return ResponseEntity.ok(post);
    }
}
