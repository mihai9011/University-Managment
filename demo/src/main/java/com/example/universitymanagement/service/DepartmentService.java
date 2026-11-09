package com.example.universitymanagement.service;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Department;
import com.example.universitymanagement.repository.Repo;

import java.util.List;

@Service
public class DepartmentService {
    private final Repo<Department> departmentRepo;

    DepartmentService(Repo<Department> departmentRepo){
        this.departmentRepo = departmentRepo;
    }

    public boolean validateName(String name){
        return name != null && !name.isBlank();
    }

    public Department save(Department department){
        if(!validateName(department.getName())){
            throw new IllegalArgumentException("Department name cannot be null or blank");
        }

        // a re-save with a new object must not lose the existing courses and teachers
        Department existing = departmentRepo.findById(department.getId());
        if(existing != null){
            department.setCourses(existing.getCourses());
            department.setTeachers(existing.getTeachers());
        }

        return departmentRepo.save(department);
    }

    public List<Department> getAll(){
        return departmentRepo.findAll();
    }

    public Department getById(String id){
        Department department = departmentRepo.findById(id);

        if(department == null){
            throw new IllegalArgumentException("Department with id " + id + " not found");
        }

        return department;
    }

    public void delete(String id){
        if(!departmentRepo.delete(id)){
            throw new IllegalArgumentException("Department with id " + id + " not found");
        }
    }
}
