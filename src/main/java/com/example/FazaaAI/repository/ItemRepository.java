package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    //Item findByUsername(String username);
}
