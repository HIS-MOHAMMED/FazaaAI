package com.example.FazaaAI.service;

import com.example.FazaaAI.dto.TopHelperDTO;
import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already registered!");
        }
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    // New method to find users by city (assuming city is part of address)
    public List<User> findUsersByCity(String city) {
        return userRepository.findByAddressContainingIgnoreCase(city);
    }

    public void updateUserReputation(User user, int pointsEarned) {
        int updatedPoints = user.getReputationPoints() + pointsEarned;
        user.setReputationPoints(updatedPoints);

        // Determine rank based on points
        if (updatedPoints >= 1000) {
            user.setRank("Diamond");
        } else if (updatedPoints >= 500) {
            user.setRank("Platinum");
        } else if (updatedPoints >= 200) {
            user.setRank("Gold");
        } else if (updatedPoints >= 50) {
            user.setRank("Silver");
        } else {
            user.setRank("Bronze");
        }

        userRepository.save(user);
    }
    public List<TopHelperDTO> getTopHelpers() {
        int limit = 10;  // Top 10 helpers

        return userRepository.findAll(
                        PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "reputationPoints"))
                ).getContent().stream()
                .map(user -> new TopHelperDTO(
                        user.getUsername(),
                        user.getReputationPoints(),
                        user.getRank()
                )).toList();
    }


}

