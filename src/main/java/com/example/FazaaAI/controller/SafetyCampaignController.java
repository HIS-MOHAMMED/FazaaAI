package com.example.FazaaAI.controller;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.SafetyCampaign;
import com.example.FazaaAI.service.CrisisService;
import com.example.FazaaAI.service.SafetyCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/safety-campaign")
public class SafetyCampaignController {

    @Autowired
    private SafetyCampaignService safetyCampaignService;

    @Autowired
    private CrisisService crisisService;

    @PostMapping("/create/{crisisId}")
    public ResponseEntity<SafetyCampaign> createCampaign(@PathVariable Long crisisId,
                                                         @RequestParam(defaultValue = "3") int expectedDurationDays,
                                                         @RequestParam(defaultValue = "High") String severityLevel) {

        Crisis crisis = crisisService.getCrisisById(crisisId);
        SafetyCampaign campaign = safetyCampaignService.createCampaign(crisis, expectedDurationDays, severityLevel);

        return ResponseEntity.ok(campaign);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SafetyCampaign>> getActiveCampaignsByCity(@RequestParam String city) {
        List<SafetyCampaign> campaigns = safetyCampaignService.getActiveCampaignsByCity(city);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/active/all")
    public ResponseEntity<List<SafetyCampaign>> getAllActiveCampaigns() {
        return ResponseEntity.ok(safetyCampaignService.getAllActiveCampaigns());
    }
}