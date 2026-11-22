package com.example.universitymanagement.repository;

import org.springframework.stereotype.Repository;

import com.example.universitymanagement.model.Enrollment;

@Repository
public class EnrollmentRepository extends InFileRepository<Enrollment>{
    EnrollmentRepository(){
        super("enrollment.json", Enrollment.class);
    };
}
