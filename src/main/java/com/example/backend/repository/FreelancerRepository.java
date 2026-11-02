

// package com.example.backend.repository;

// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.backend.model.Freelancer;

// package com.example.backend.repository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.example.backend.model.Freelancer;

// public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {

//     Optional<Freelancer> findByUser_Email(String email);

//     @Query("""
//         SELECT f FROM Freelancer f
//         WHERE
//         (:keyword IS NULL OR
//          LOWER(f.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
//          LOWER(f.bio) LIKE LOWER(CONCAT('%', :keyword, '%')))
//         AND
//         (:location IS NULL OR
//          LOWER(f.city) LIKE LOWER(CONCAT('%', :location, '%')) OR
//          LOWER(f.state) LIKE LOWER(CONCAT('%', :location, '%')) OR
//          LOWER(f.country) LIKE LOWER(CONCAT('%', :location, '%')))
//     """)
//     List<Freelancer> searchFreelancers(
//             @Param("keyword") String keyword,
//             @Param("location") String location
//     );
// }
package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Freelancer;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {

    Optional<Freelancer> findByUser_Email(String email);

    @Query("SELECT f FROM Freelancer f WHERE " +
           "(:keyword IS NULL OR LOWER(f.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(f.bio) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:experience IS NULL OR LOWER(f.experience) LIKE LOWER(CONCAT('%', :experience, '%'))) " +
           "AND (:location IS NULL OR LOWER(f.city) LIKE LOWER(CONCAT('%', :location, '%')) " +
           "OR LOWER(f.state) LIKE LOWER(CONCAT('%', :location, '%')) " +
           "OR LOWER(f.country) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Freelancer> searchFreelancers(
            @Param("keyword") String keyword,
            @Param("experience") String experience,
            @Param("location") String location
    );
}