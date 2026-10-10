package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Freelancer;
import com.example.backend.model.FreelancerJob;
import com.example.backend.model.User;
import com.example.backend.repository.FreelancerJobRepository;
import com.example.backend.repository.FreelancerRepository;
import com.example.backend.repository.UserRepository;

@Service
public class FreelancerJobService {

    private final FreelancerJobRepository freelancerJobRepository;
    private final FreelancerRepository freelancerRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public FreelancerJobService(
            FreelancerJobRepository freelancerJobRepository,
            FreelancerRepository freelancerRepository,
            UserRepository userRepository,
            NotificationService notificationService) {

        this.freelancerJobRepository = freelancerJobRepository;
        this.freelancerRepository = freelancerRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // Create a new freelancer availability post
    @Transactional
    public FreelancerJob createJob(FreelancerJob job, String email) {

        if (email == null || email.isBlank()) {
            throw new RuntimeException(
                    "Authenticated user email is missing"
            );
        }

        Freelancer freelancer = freelancerRepository
                .findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException(
                        "Freelancer profile not found for the logged-in account. "
                        + "Please ensure your account has a Freelancer profile."
                ));

        job.setFreelancer(freelancer);

        return freelancerJobRepository.save(job);
    }

    // Get all freelancer availability posts
    @Transactional(readOnly = true)
    public List<FreelancerJob> getAllJobs() {
        return freelancerJobRepository.findAll();
    }

    // Get one freelancer availability post
    @Transactional(readOnly = true)
    public Optional<FreelancerJob> getJobById(Long id) {
        return freelancerJobRepository.findById(id);
    }

    // NEW: Client sends an interest request to a freelancer
    @Transactional
    public void notifyFreelancerOfClientInterest(
            Long freelancerJobId,
            String clientEmail) {

        if (clientEmail == null || clientEmail.isBlank()) {
            throw new RuntimeException(
                    "Authenticated client email is missing"
            );
        }

        // Find the selected freelancer post
        FreelancerJob freelancerJob = freelancerJobRepository
                .findById(freelancerJobId)
                .orElseThrow(() -> new RuntimeException(
                        "Freelancer post not found"
                ));

        // Find the logged-in client
        User client = userRepository
                .findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException(
                        "Client account not found"
                ));

        // Get the freelancer who owns this post
        User freelancerUser = freelancerJob
                .getFreelancer()
                .getUser();

        // Prevent a user from requesting their own post
        if (freelancerUser.getEmail().equalsIgnoreCase(clientEmail)) {
            throw new RuntimeException(
                    "You cannot apply to your own freelancer post"
            );
        }

        // Save notification for the freelancer
        notificationService.createNotification(
                freelancerUser,
                client.getName()
                        + " is interested in your freelancer post: "
                        + freelancerJob.getTitle(),
                "CLIENT_INTEREST",
                freelancerJob.getId()
        );
    }

    // Delete a freelancer availability post
    @Transactional
    public void deleteJob(Long id) {

        if (!freelancerJobRepository.existsById(id)) {
            throw new RuntimeException(
                    "Freelancer availability post not found"
            );
        }

        freelancerJobRepository.deleteById(id);
    }
}