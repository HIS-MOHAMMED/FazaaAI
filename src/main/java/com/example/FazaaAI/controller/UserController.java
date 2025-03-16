package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.service.UserService;
import com.example.FazaaAI.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Register a user
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    // ✅ Login with JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (user.isPresent()) {
            String token = jwtUtil.generateToken(user.get().getId(), user.get().getEmail());
            return ResponseEntity.ok(token); // Return JWT token
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

    // ✅ Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // ✅ Get current authenticated user (/me)
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(null);
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body(null);
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}