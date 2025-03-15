package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.service.CrisisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crisis")
public class CrisisController {

    @Autowired
    private CrisisService crisisService;

    // Report a crisis and process it with AI
    @PostMapping("/report")
    public ResponseEntity<Crisis> reportCrisis(@RequestBody Crisis crisis) {
        Crisis processedCrisis = crisisService.processCrisisReport(crisis);
        return ResponseEntity.ok(processedCrisis);
    }

    // Mark a crisis as resolved
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Crisis> resolveCrisis(@PathVariable Long id) {
        Crisis updatedCrisis = crisisService.resolveCrisis(id);
        return ResponseEntity.ok(updatedCrisis);
    }
}