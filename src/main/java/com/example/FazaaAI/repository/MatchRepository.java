package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  MatchRepository extends JpaRepository<Match, Long> {
   // Match findByUsername(String username);
}
