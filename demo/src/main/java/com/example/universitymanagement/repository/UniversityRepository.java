package com.example.universitymanagement.repository;

import org.springframework.stereotype.Repository;

import com.example.universitymanagement.model.University;

@Repository
public class UniversityRepository extends InFileRepository<University> {
    UniversityRepository(){
        super("university.json", University.class);
    }
}
