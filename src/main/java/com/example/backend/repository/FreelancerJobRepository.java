package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.FreelancerJob;

public interface FreelancerJobRepository extends JpaRepository<FreelancerJob, Long> {
}