package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.JobDescriptionRequest;
import com.example.backend.service.GeminiService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class JobDescriptionController {

    private final GeminiService geminiService;

    public JobDescriptionController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generate-job-description")
    public String generateJobDescription(
            @RequestBody JobDescriptionRequest request) {

        return geminiService.generateJobDescription(
                request.getProjectIdea()
        );
    }
}