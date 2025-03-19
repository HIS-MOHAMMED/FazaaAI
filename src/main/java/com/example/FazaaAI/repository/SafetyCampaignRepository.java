package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.SafetyCampaign;
import com.example.FazaaAI.entity.Crisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SafetyCampaignRepository extends JpaRepository<SafetyCampaign, Long> {

    Optional<SafetyCampaign> findByCrisis(Crisis crisis);

    List<SafetyCampaign> findByCrisis_CityAndEndDateAfter(String city, LocalDateTime now);

    List<SafetyCampaign> findByEndDateAfter(LocalDateTime now);
}