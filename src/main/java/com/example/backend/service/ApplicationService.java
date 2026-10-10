package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Application;
import com.example.backend.model.Client;
import com.example.backend.model.ClientJob;
import com.example.backend.model.Freelancer;
import com.example.backend.repository.ApplicationRepository;
import com.example.backend.repository.ClientJobRepository;
import com.example.backend.repository.ClientRepository;
import com.example.backend.repository.FreelancerRepository;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ClientJobRepository clientJobRepository;
    private final ClientRepository clientRepository;
    private final FreelancerRepository freelancerRepository;
    private final NotificationService notificationService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            ClientJobRepository clientJobRepository,
            ClientRepository clientRepository,
            FreelancerRepository freelancerRepository,
            NotificationService notificationService) {

        this.applicationRepository = applicationRepository;
        this.clientJobRepository = clientJobRepository;
        this.clientRepository = clientRepository;
        this.freelancerRepository = freelancerRepository;
        this.notificationService = notificationService;
    }

    // Freelancer applies for a client job
    @Transactional
    public Application applyForJob(Long jobId, String email) {
System.out.println("========== APPLY DEBUG ==========");
System.out.println("Authenticated email: [" + email + "]");
        Freelancer freelancer = freelancerRepository
                .findByUser_Email(email)
                .orElseThrow(() ->
                        new RuntimeException("Freelancer profile not found"));

        ClientJob clientJob = clientJobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Client job not found"));

        boolean alreadyApplied =
                applicationRepository
                        .existsByClientJob_IdAndFreelancer_Id(
                                jobId,
                                freelancer.getId()
                        );

        if (alreadyApplied) {
            throw new RuntimeException(
                    "You have already applied for this job");
        }

        Application application = new Application();
        application.setClientJob(clientJob);
        application.setFreelancer(freelancer);
        application.setStatus("PENDING");

        Application savedApplication =
                applicationRepository.save(application);

        // Notify the client who owns this job
        notificationService.notifyClient(savedApplication);

        return savedApplication;
    }

    // Freelancer views their own applications
    public List<Application> getMyApplications(String email) {

System.out.println("Apply request email: [" + email + "]");

Freelancer freelancer = freelancerRepository
        .findByUser_Email(email.trim())
        .orElseThrow(() ->
                new RuntimeException(
                        "Freelancer profile not found for email: " + email
                ));

        return applicationRepository
                .findByFreelancer_Id(freelancer.getId());
    }

    // Client views applications for their own job
    public List<Application> getApplicationsForMyJob(
            Long jobId,
            String email) {

        Client client = clientRepository
                .findByUser_Email(email)
                .orElseThrow(() ->
                        new RuntimeException("Client profile not found"));

        ClientJob job = clientJobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Client job not found"));

        if (!job.getClient().getId().equals(client.getId())) {
            throw new RuntimeException(
                    "You are not authorized to view these applications");
        }

        return applicationRepository.findByClientJob_Id(jobId);
    }

    // Client accepts or rejects an application
    @Transactional
    public Application updateApplicationStatus(
            Long applicationId,
            String status,
            String email) {

        if (!"ACCEPTED".equals(status)
                && !"REJECTED".equals(status)) {
            throw new RuntimeException("Invalid application status");
        }

        Client client = clientRepository
                .findByUser_Email(email)
                .orElseThrow(() ->
                        new RuntimeException("Client profile not found"));

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));

        if (!application.getClientJob().getClient()
                .getId().equals(client.getId())) {
            throw new RuntimeException(
                    "You are not authorized to update this application");
        }

        if (!"PENDING".equals(application.getStatus())) {
            throw new RuntimeException(
                    "This application has already been reviewed");
        }

        application.setStatus(status);

        Application savedApplication =
                applicationRepository.save(application);

        // Notify the freelancer about the decision
        notificationService.notifyFreelancer(savedApplication);

        return savedApplication;
    }
}