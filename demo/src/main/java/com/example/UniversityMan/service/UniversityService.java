package com.example.UniversityMan.service;

import org.springframework.stereotype.Service;
import com.example.UniversityMan.repository.UniversityRepository;

@Service
public class UniversityService {
    private final UniversityRepository universityRepo;

    UniversityService(UniversityRepository universityRepo){
        this.universityRepo = universityRepo;
    }

    public boolean validateName(String name){
        return name != null && !name.isBlank();
    }
}