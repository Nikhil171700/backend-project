
package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.model.Client;
import com.example.backend.model.Freelancer;
import com.example.backend.model.User;
import com.example.backend.repository.ClientRepository;
import com.example.backend.repository.FreelancerRepository;
import com.example.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final FreelancerRepository freelancerRepository;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            ClientRepository clientRepository,
            FreelancerRepository freelancerRepository,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.freelancerRepository = freelancerRepository;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return "Account not found";
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return "Incorrect password";
        }

        return jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );
    }

    @Transactional
    public String register(RegisterRequest request) {

        if (request.getEmail() == null
                || request.getEmail().isBlank()) {
            return "Email is required";
        }

        if (request.getRole() == null
                || (!"CLIENT".equals(request.getRole())
                && !"FREELANCER".equals(request.getRole()))) {
            return "Invalid role";
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered";
        }

        // Create the user account
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        // Create the corresponding profile
        if ("CLIENT".equals(request.getRole())) {

            Client client = new Client();
            client.setUser(savedUser);
            client.setCompanyName(request.getCompanyName());
            client.setCompanyDescription(
                    request.getCompanyDescription());
            client.setCompanyWebsite(request.getCompanyWebsite());
            client.setCountry(request.getCountry());
            client.setState(request.getState());
            client.setCity(request.getCity());

            clientRepository.save(client);

        } else {

            Freelancer freelancer = new Freelancer();
            freelancer.setUser(savedUser);
            freelancer.setSkills(request.getSkills());
            freelancer.setExperience(request.getExperience());
            freelancer.setBio(request.getBio());
            freelancer.setCountry(request.getCountry());
            freelancer.setState(request.getState());
            freelancer.setCity(request.getCity());

            freelancerRepository.save(freelancer);
        }

        return "Registration successful";
    }
}