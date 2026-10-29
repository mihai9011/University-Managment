package com.example.UniversityMan.service;

import org.springframework.stereotype.Service;
import com.example.UniversityMan.repository.DepartmentRepository;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepo;

    DepartmentService(DepartmentRepository departmentRepo){
        this.departmentRepo = departmentRepo;
    }

    public boolean validateName(String name){
        return name != null && !name.isBlank();
    }
}
