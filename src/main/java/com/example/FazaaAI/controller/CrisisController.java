package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.service.CrisisService;
import com.example.FazaaAI.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crisis")
public class CrisisController {

    @Autowired
    private CrisisService crisisService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Crisis> createCrisis(@RequestBody Crisis crisis, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        User user = userService.getUserById(userId);

        crisis.setUser(user); // ✅ Attach the user before processing

        Crisis createdCrisis = crisisService.processCrisisReport(crisis);

        return ResponseEntity.ok(createdCrisis);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Crisis>> getAllCrisis() {
        List<Crisis> crisisList = crisisService.getAllCrisis();
        return ResponseEntity.ok(crisisList);
    }
}