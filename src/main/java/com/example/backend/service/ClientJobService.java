package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.backend.model.Client;
import com.example.backend.model.ClientJob;
import com.example.backend.repository.ClientJobRepository;
import com.example.backend.repository.ClientRepository;

@Service
public class ClientJobService {

    private final ClientJobRepository clientJobRepository;
    private final ClientRepository clientRepository;

    public ClientJobService(
            ClientJobRepository clientJobRepository,
            ClientRepository clientRepository) {

        this.clientJobRepository = clientJobRepository;
        this.clientRepository = clientRepository;
    }

    // Create a new client job
    public ClientJob createJob(ClientJob job, String email) {

        Client client = clientRepository
                .findByUser_Email(email)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        // Connect the job to the logged-in client
        job.setClient(client);

        return clientJobRepository.save(job);
    }

    // Get all client jobs
    public List<ClientJob> getAllJobs() {
        return clientJobRepository.findAll();
    }

    // Get one client job
    public Optional<ClientJob> getJobById(Long id) {
        return clientJobRepository.findById(id);
    }

    // Delete a client job
    public void deleteJob(Long id) {

        if (!clientJobRepository.existsById(id)) {
            throw new RuntimeException("Client job not found");
        }

        clientJobRepository.deleteById(id);
    }
}