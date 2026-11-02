package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.FreelancerSearchDTO;
import com.example.backend.dto.FreelancerSearchRequest;
import com.example.backend.service.FreelancerSearchService;
import com.example.backend.service.GeminiService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class FreelancerSearchController {

    private final FreelancerSearchService freelancerSearchService;
    private final GeminiService geminiService;

    public FreelancerSearchController(
            FreelancerSearchService freelancerSearchService,
            GeminiService geminiService) {

        this.freelancerSearchService = freelancerSearchService;
        this.geminiService = geminiService;
    }

    // Normal freelancer search
    @GetMapping("/search-freelancers")
    public List<FreelancerSearchDTO> searchFreelancers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String location) {

        return freelancerSearchService.searchFreelancers(
                keyword, experience, location);
    }

    // Gemini-powered freelancer search
    @PostMapping("/smart-search")
public List<FreelancerSearchDTO> smartSearch(
        @RequestBody String userMessage) {

    String geminiResponse =
            geminiService.understandFreelancerSearch(userMessage);

    FreelancerSearchRequest request =
            geminiService.parseSearchResponse(geminiResponse);

    // Print the filters extracted by Gemini
    System.out.println("Gemini filters: " +
            request.getKeyword() + " | " +
            request.getExperience() + " | " +
            request.getLocation());

    return freelancerSearchService.searchUsingFilters(request);
}
}