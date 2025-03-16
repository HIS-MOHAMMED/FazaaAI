package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.MatchRequest;
import com.example.FazaaAI.service.MatchRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match-request")
public class MatchRequestController {

    @Autowired
    private MatchRequestService matchRequestService;

    @GetMapping("/my")
    public ResponseEntity<List<MatchRequest>> getMyMatches(HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        List<MatchRequest> matches = matchRequestService.getUserMatchRequests(userId);

        return ResponseEntity.ok(matches);
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<MatchRequest> acceptMatch(@PathVariable Long id, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        MatchRequest match = matchRequestService.acceptMatch(id, userId);

        return ResponseEntity.ok(match);
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<MatchRequest> rejectMatch(@PathVariable Long id, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        MatchRequest match = matchRequestService.rejectMatch(id, userId);

        return ResponseEntity.ok(match);
    }
}