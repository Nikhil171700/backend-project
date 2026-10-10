// package com.example.backend.repository;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.backend.model.Freelancer;

// public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
// }

package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Freelancer;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {

    Optional<Freelancer> findByUser_Email(String email);
}