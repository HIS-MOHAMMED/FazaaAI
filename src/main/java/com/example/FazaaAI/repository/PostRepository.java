package com.example.FazaaAI.repository;

import com.example.FazaaAI.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTypeAndStatus(String type, String status);

    List<Post> findByCityAndTypeAndStatus(String city, String type, String status);
}
