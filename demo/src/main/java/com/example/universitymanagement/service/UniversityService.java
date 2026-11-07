package com.example.universitymanagement.service;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.University;
import com.example.universitymanagement.repository.Repo;

import java.util.List;


@Service
public class UniversityService {
    private final Repo<University> universityRepo;

    UniversityService(Repo<University> universityRepo){
        this.universityRepo = universityRepo;
    }

    public boolean validateName(String name){
        return name != null && !name.isBlank();
    }

    public University save(University university){
        if(!validateName(university.getName())){
            throw new IllegalArgumentException("University name cannot be null or blank");
        }

        return universityRepo.save(university);
    }

    public List<University> getAll(){
        return universityRepo.findAll();
    }

    public University getById(String id){
        University university = universityRepo.findById(id);

        if(university == null){
            throw new IllegalArgumentException("University with id " + id + " not found");
        }

        return university;
    }

    public void delete(String id){
        if(!universityRepo.delete(id)){
            throw new IllegalArgumentException("University with id " + id + " not found");
        }
    }
}