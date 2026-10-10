package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByFreelancer_Id(Long freelancerId);

    List<Application> findByClientJob_Id(Long clientJobId);

    boolean existsByClientJob_IdAndFreelancer_Id(
            Long clientJobId,
            Long freelancerId
    );
}