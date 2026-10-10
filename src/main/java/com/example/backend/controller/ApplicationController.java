package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Application;
import com.example.backend.service.ApplicationService;

@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Freelancer applies for a client job
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<?> applyForJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        try {
            Application application =
                    applicationService.applyForJob(
                            jobId,
                            authentication.getName()
                    );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(application);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // Freelancer views their own applications
    @GetMapping("/my")
    public List<Application> getMyApplications(
            Authentication authentication) {

        return applicationService.getMyApplications(
                authentication.getName()
        );
    }

    // Client views applications for their own job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getApplicationsForMyJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        try {
            return ResponseEntity.ok(
                    applicationService.getApplicationsForMyJob(
                            jobId,
                            authentication.getName()
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // Client accepts or rejects an application
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        try {
            String status = request.get("status");

            Application application =
                    applicationService.updateApplicationStatus(
                            applicationId,
                            status,
                            authentication.getName()
                    );

            return ResponseEntity.ok(application);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}