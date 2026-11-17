package com.example.universitymanagement.repository;

import org.springframework.stereotype.Repository;

import com.example.universitymanagement.model.Department;

@Repository
public class DepartmentRepository extends InFileRepository<Department> {
    DepartmentRepository(){
        super("department.json", Department.class);
    }
}
