package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    //Post findByUsername(String username);
}
