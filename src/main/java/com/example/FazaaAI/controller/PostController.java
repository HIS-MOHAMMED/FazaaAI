package com.example.FazaaAI.controller;

import com.example.FazaaAI.dto.PostResponseDTO;
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
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        List<Post> posts = postService.getAllPosts();

        // Convert each Post to PostResponseDTO
        List<PostResponseDTO> postDTOs = posts.stream().map(post -> {
            PostResponseDTO dto = new PostResponseDTO();

            dto.setId(post.getId());
            dto.setUserDescription(post.getUserDescription());
            dto.setEnhancedDescription(post.getEnhancedDescription());
            dto.setStatus(post.getStatus());
            dto.setType(post.getType());
            dto.setCity(post.getCity());
            dto.setPhoneNumber(post.getPhoneNumber());

            // Extract user details safely
            if (post.getUser() != null) {
                dto.setUserId(post.getUser().getId());
                dto.setUsername(post.getUser().getUsername()); // Optional if you want to display it
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(postDTOs);
    }
}