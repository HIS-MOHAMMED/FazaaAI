package com.example.FazaaAI.controller;

import com.example.FazaaAI.dto.CrisisResponseDTO;
import com.example.FazaaAI.entity.Crisis;
import com.example.FazaaAI.entity.User;
import com.example.FazaaAI.service.CrisisService;
import com.example.FazaaAI.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crisis")
public class CrisisController {

    @Autowired
    private CrisisService crisisService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<CrisisResponseDTO> createCrisis(@RequestBody Crisis crisis, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.getUserById(userId);
        crisis.setUser(user);

        Crisis savedCrisis = crisisService.processCrisisReport(crisis);

        CrisisResponseDTO dto = new CrisisResponseDTO(
                savedCrisis.getUser().getUsername(),
                savedCrisis.getId(),
                savedCrisis.getEndDate(),
                savedCrisis.getStartDate(),
                savedCrisis.getSafetyCheckDurationDays(),
                savedCrisis.getSurvivalGuide(),
                savedCrisis.getCity(),
                savedCrisis.getType(),
                savedCrisis.getEnhancedDescription(),
                savedCrisis.getUserDescription(),
                savedCrisis.getUser().getId());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CrisisResponseDTO>> getAllCrisis() {
        List<Crisis> crisisList = crisisService.getAllCrisis();

        List<CrisisResponseDTO> dtoList = crisisList.stream()
                .map(crisisService::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}