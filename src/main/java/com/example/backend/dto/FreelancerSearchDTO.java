package com.example.backend.dto;

public class FreelancerSearchDTO {

    private Long id;
    private String name;
    private String skills;
    private String experience;
    private String bio;
    private String city;
    private String state;
    private String country;

    public FreelancerSearchDTO(Long id, String name, String skills,
                               String experience, String bio,
                               String city, String state, String country) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.experience = experience;
        this.bio = bio;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSkills() {
        return skills;
    }

    public String getExperience() {
        return experience;
    }

    public String getBio() {
        return bio;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
}