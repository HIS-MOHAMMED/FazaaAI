package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.MatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

    List<MatchRequest> findByRequestPostUserIdOrOfferPostUserId(Long requestUserId, Long offerUserId);

    List<MatchRequest> findByStatus(String status);
}