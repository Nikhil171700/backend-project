package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.FreelancerJob;
import com.example.backend.service.FreelancerJobService;

@RestController
@RequestMapping("/freelancer-jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class FreelancerJobController {

    private final FreelancerJobService freelancerJobService;

    public FreelancerJobController(FreelancerJobService freelancerJobService) {
        this.freelancerJobService = freelancerJobService;
    }

    // Create a freelancer job/availability post
    @PostMapping
    public FreelancerJob createJob(
            @RequestBody FreelancerJob job,
            Authentication authentication) {

        String email = authentication.getName();

        return freelancerJobService.createJob(job, email);
    }

    // Get all freelancer job posts
    @GetMapping
    public List<FreelancerJob> getAllJobs() {
        return freelancerJobService.getAllJobs();
    }
    // Client requests a freelancer
@PostMapping("/{id}/apply")
public ResponseEntity<?> applyToFreelancer(
        @PathVariable Long id,
        Authentication authentication) {

    if (authentication == null) {
        return ResponseEntity.status(401)
                .body(Map.of("error", "Please log in"));
    }

    try {
        freelancerJobService.notifyFreelancerOfClientInterest(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Your request has been sent to the freelancer."
                )
        );

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
    }
}

    // Get a single freelancer job post
    @GetMapping("/{id}")
    public ResponseEntity<FreelancerJob> getJobById(
            @PathVariable Long id) {

        return freelancerJobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a freelancer job post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {

        freelancerJobService.deleteJob(id);

        return ResponseEntity.noContent().build();
    }
}