package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.Crisis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrisisRepository extends JpaRepository<Crisis, Long> {
    //Crisis findByUsername(String username);
}
