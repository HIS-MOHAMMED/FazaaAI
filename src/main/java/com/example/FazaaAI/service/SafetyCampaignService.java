package com.example.FazaaAI.service;

import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.SafetyCampaign;
import com.example.FazaaAI.repository.CrisisRepository;
import com.example.FazaaAI.repository.SafetyCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SafetyCampaignService {

    @Autowired
    private SafetyCampaignRepository safetyCampaignRepository;

    @Autowired
    private CrisisRepository crisisRepository;

    public SafetyCampaign createCampaign(Crisis crisis, int durationDays, String severityLevel) {
        SafetyCampaign campaign = new SafetyCampaign();

        campaign.setCrisis(crisis);
        LocalDateTime startDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime endDateTime = startDateTime.plusDays(durationDays);

        campaign.setStartDate(startDateTime);
        campaign.setEndDate(endDateTime);
        campaign.setSeverityLevel(severityLevel);

        return safetyCampaignRepository.save(campaign);
    }

    public SafetyCampaign getCampaignByCrisis(Crisis crisis) {
        return safetyCampaignRepository.findByCrisis(crisis)
                .orElseThrow(() -> new RuntimeException("No campaign found for this crisis."));
    }

    public List<SafetyCampaign> getActiveCampaignsByCity(String city) {
        return safetyCampaignRepository.findByCrisis_CityAndEndDateAfter(city, LocalDateTime.now());
    }

    public List<SafetyCampaign> getAllActiveCampaigns() {
        return safetyCampaignRepository.findByEndDateAfter(LocalDateTime.now());
    }
}