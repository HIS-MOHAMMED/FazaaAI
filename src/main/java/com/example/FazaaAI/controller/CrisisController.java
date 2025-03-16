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

    // ✅ Create a Crisis post (AI classifies type, enhancedDescription, survivalGuide)
    @PostMapping("/create")
    public ResponseEntity<Crisis> createCrisis(@RequestBody Crisis crisis, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        User user = userService.getUserById(userId);
        crisis.setUser(user);

        Crisis createdCrisis = crisisService.createCrisis(crisis);

        return ResponseEntity.ok(createdCrisis);
    }

    // ✅ Get all crisis posts (optional)
    @GetMapping("/all")
    public ResponseEntity<List<Crisis>> getAllCrisis() {
        List<Crisis> crisisList = crisisService.getAllCrisis();
        return ResponseEntity.ok(crisisList);
    }

    // ✅ Get survival guide for a specific crisis post
    @GetMapping("/{id}/survival-guide")
    public ResponseEntity<String> getSurvivalGuide(@PathVariable Long id) {
        Crisis crisis = crisisService.getCrisisById(id);
        return ResponseEntity.ok(crisis.getSurvivalGuide());
    }

//    // ✅ Optional: Update crisis status manually (if you need it)
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Crisis> updateStatus(@PathVariable Long id, @RequestParam String status) {
//        Crisis updatedCrisis = crisisService.updateCrisisStatus(id, status);
//        return ResponseEntity.ok(updatedCrisis);
//    }
}
