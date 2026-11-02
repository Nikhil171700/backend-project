package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.FreelancerDescriptionRequest;
import com.example.backend.service.GeminiService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class FreelancerDescriptionController {

    private final GeminiService geminiService;

    public FreelancerDescriptionController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generate-freelancer-description")
    public String generateFreelancerDescription(
            @RequestBody FreelancerDescriptionRequest request) {

        return geminiService.generateFreelancerDescription(
                request.getFreelancerDetails()
        );
    }
}