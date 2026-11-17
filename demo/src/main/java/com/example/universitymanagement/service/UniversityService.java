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

        // a re-save with a new object must not lose the existing departments and rooms
        University existing = universityRepo.findById(university.getId());
        if(existing != null){
            university.setDepartments(existing.getDepartments());
            university.setRooms(existing.getRooms());
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
        University university = getById(id);

        // departments and rooms are real data, so they are not deleted with it and must not point to a missing university
        if(!university.getDepartments().isEmpty() || !university.getRooms().isEmpty()){
            throw new IllegalArgumentException("University with id " + id + " still has departments or rooms");
        }

        universityRepo.delete(id);
    }
}