package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUser_Email(String email);
}