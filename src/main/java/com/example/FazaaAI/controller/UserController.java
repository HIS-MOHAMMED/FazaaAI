package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.service.UserService;
import com.example.FazaaAI.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (user.isPresent()) {
            String token = jwtUtil.generateToken(user.get().getId(), user.get().getEmail());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(null);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body(null);
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }
    @GetMapping("/leaderboard")
    public ResponseEntity<List<User>> getLeaderboard() {
        List<User> topHelpers = userService.getTopHelpers();
        return ResponseEntity.ok(topHelpers);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        int reputationPoints = user.getReputationPoints();
        String rank = user.getRank();

        // Calculate points to next rank
        int pointsToNextRank = 0;
        if (reputationPoints < 50) {
            pointsToNextRank = 50 - reputationPoints;
        } else if (reputationPoints < 200) {
            pointsToNextRank = 200 - reputationPoints;
        } else if (reputationPoints < 500) {
            pointsToNextRank = 500 - reputationPoints;
        } else if (reputationPoints < 1000) {
            pointsToNextRank = 1000 - reputationPoints;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("reputationPoints", reputationPoints);
        response.put("rank", rank);
        response.put("pointsToNextRank", pointsToNextRank);

        return ResponseEntity.ok(response);
    }
}
