package com.example.UniversityMan.service;

import org.springframework.stereotype.Service;
import com.example.UniversityMan.repository.DepartmentRepository;
import java.util.List;
import com.example.UniversityMan.model.Department;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepo;

    DepartmentService(DepartmentRepository departmentRepo){
        this.departmentRepo = departmentRepo;
    }

    public boolean validateName(String name){
        return name != null && !name.isBlank();
    }

    public Department save(Department department){
        if(!validateName(department.getName())){
            throw new IllegalArgumentException("Department name cannot be null or blank");
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
