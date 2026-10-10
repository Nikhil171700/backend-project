package com.example.backend.controller;

import java.util.List;

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

import com.example.backend.model.ClientJob;
import com.example.backend.service.ClientJobService;

@RestController
@RequestMapping("/client-jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class ClientJobController {

    private final ClientJobService clientJobService;

    public ClientJobController(ClientJobService clientJobService) {
        this.clientJobService = clientJobService;
    }

    // Create a client job
    @PostMapping
    public ClientJob createJob(
            @RequestBody ClientJob job,
            Authentication authentication) {

        String email = authentication.getName();

        return clientJobService.createJob(job, email);
    }

    // Get all client jobs
    @GetMapping
    public List<ClientJob> getAllJobs() {
        return clientJobService.getAllJobs();
    }

    // Get one client job
    @GetMapping("/{id}")
    public ResponseEntity<ClientJob> getJobById(
            @PathVariable Long id) {

        return clientJobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a client job
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {

        clientJobService.deleteJob(id);

        return ResponseEntity.noContent().build();
    }
}