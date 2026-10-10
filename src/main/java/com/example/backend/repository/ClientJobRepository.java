package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.ClientJob;

public interface ClientJobRepository extends JpaRepository<ClientJob, Long> {
}