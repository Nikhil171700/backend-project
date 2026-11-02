package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.FreelancerSearchDTO;
import com.example.backend.dto.FreelancerSearchRequest;
import com.example.backend.model.Freelancer;
import com.example.backend.repository.FreelancerRepository;

@Service
public class FreelancerSearchService {

    private final FreelancerRepository freelancerRepository;

    public FreelancerSearchService(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    // Search freelancers using filters
    public List<FreelancerSearchDTO> searchFreelancers(
            String keyword, String experience, String location) {

        List<Freelancer> freelancers =
                freelancerRepository.searchFreelancers(
                        keyword, experience, location);

        return freelancers.stream()
                .map(f -> new FreelancerSearchDTO(
                        f.getId(),
                        f.getUser().getName(),
                        f.getSkills(),
                        f.getExperience(),
                        f.getBio(),
                        f.getCity(),
                        f.getState(),
                        f.getCountry()
                ))
                .toList();
    }

    // Search freelancers using Gemini-extracted filters
    public List<FreelancerSearchDTO> searchUsingFilters(
            FreelancerSearchRequest request) {

        return searchFreelancers(
                request.getKeyword(),
                request.getExperience(),
                request.getLocation()
        );
    }
}