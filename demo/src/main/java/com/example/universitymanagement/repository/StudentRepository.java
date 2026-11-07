package com.example.universitymanagement.repository;

import org.springframework.stereotype.Repository;

import com.example.universitymanagement.model.Student;

@Repository
public class StudentRepository extends BaseRepo<Student>{
    StudentRepository(){};
}
