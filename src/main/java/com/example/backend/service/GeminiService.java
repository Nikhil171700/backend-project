package com.example.backend.service;

import org.springframework.stereotype.Service;

import com.example.backend.dto.FreelancerSearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiService {

    private final Client geminiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiService(Client geminiClient) {
        this.geminiClient = geminiClient;
    }

    // Generate response using Gemini
    public String generateResponse(String prompt) {

        System.out.println("Sending request to Gemini...");

        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        "gemini-3.8-flash",
                        prompt,
                        null
                );

        System.out.println("Gemini response received!");

        return response.text();
    }

    // Extract freelancer search filters
    public String understandFreelancerSearch(String userMessage) {

        String prompt = """
                Extract freelancer search filters from the user's message.

                Return ONLY valid JSON with these three fields:
                {
                  "keyword": "skill or technology",
                  "experience": "experience level or range",
                  "location": "city, state or country"
                }

                Use null for any missing field.
                Do not invent information.

                Do not include words like 'years' or 'year'
                in the experience value.

                User message:
                """ + userMessage;

        return generateResponse(prompt);
    }

    // Parse Gemini response and normalize filters
    public FreelancerSearchRequest parseSearchResponse(String response) {

        try {

            response = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            FreelancerSearchRequest request =
                    objectMapper.readValue(
                            response,
                            FreelancerSearchRequest.class
                    );

            // Normalize experience
            if (request.getExperience() != null) {

                String experience = request.getExperience()
                        .replace(" years", "")
                        .replace(" year", "")
                        .trim();

                request.setExperience(experience);
            }

            System.out.println("Parsed Gemini filters: " +
                    request.getKeyword() + " | " +
                    request.getExperience() + " | " +
                    request.getLocation());

            return request;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Gemini response", e
            );
        }
    }
    // Generate professional client job description
public String generateJobDescription(String projectIdea) {

    String prompt = """
            You are a professional job description writer.

            Create a professional and clear job description
            based on the client's project idea.

            Include:
            1. Project Overview
            2. Responsibilities
            3. Required Skills
            4. Experience Requirements
            5. Expected Deliverables

            Do not invent specific project details.
            Keep the description professional and easy to understand.

            Client's Project Idea:
            """ + projectIdea;

    return generateResponse(prompt);
}
// Generate professional freelancer description
public String generateFreelancerDescription(String freelancerDetails) {

    String prompt = """
            You are a professional freelancer profile writer.

            Create a professional freelancer description
            based on the details provided.

            Include:
            1. Professional Introduction
            2. Technical Skills
            3. Experience
            4. Project Experience
            5. Services Offered

            Do not invent skills, experience or projects.
            Use only the information provided.
            Keep the description professional and attractive to clients.

            Freelancer Details:
            """ + freelancerDetails;

    return generateResponse(prompt);
}
// AI chatbot for clients and freelancers
public String chat(String message, String role) {

    String prompt = """
            You are an AI assistant for a Freelancer-Client Marketplace.

            The current user's role is: %s

            If the user is a CLIENT:
            - Help them understand how to hire freelancers.
            - Help with project requirements and job postings.
            - Answer questions about managing projects.

            If the user is a FREELANCER:
            - Help them understand how to find projects.
            - Help with applications and professional profiles.
            - Answer questions about freelancing.

            Give simple, clear and helpful answers.
            Do not claim to access marketplace database records
            unless actual database information is provided.

            User message:
            %s
            """.formatted(role, message);

    return generateResponse(prompt);
}
}