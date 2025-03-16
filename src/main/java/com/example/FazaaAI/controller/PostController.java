package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.Post;
import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.service.PostService;
import com.example.FazaaAI.service.UserService;
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

    @Autowired
    private UserService userService;

    // ✅ Create a new post (AI + matching happens automatically)
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        // Attach the user to the post
        User user = userService.getUserById(userId);
        post.setUser(user);

        Post createdPost = postService.createPost(post);

        return ResponseEntity.ok(createdPost);
    }

    // ✅ Mark post as done manually (optional)
    @PutMapping("/resolve/{id}")
    public ResponseEntity<Post> resolvePost(@PathVariable Long id, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        Post updatedPost = postService.markPostAsDone(id);

        return ResponseEntity.ok(updatedPost);
    }

    // ✅ Get all posts
    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        List<Post> posts = postService.getAllPosts();

        return ResponseEntity.ok(posts);
    }
}