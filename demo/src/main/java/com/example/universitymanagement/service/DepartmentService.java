package com.example.universitymanagement.service;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Department;
import com.example.universitymanagement.model.University;
import com.example.universitymanagement.repository.Repo;

import java.util.List;

@Service
public class DepartmentService {
    private final Repo<Department> departmentRepo;
    private final Repo<University> universityRepo;

    DepartmentService(Repo<Department> departmentRepo, Repo<University> universityRepo){
        this.departmentRepo = departmentRepo;
        this.universityRepo = universityRepo;
    }

    public boolean validateName(String name){
        return name != null && !name.isBlank();
    }

    public boolean validateUniversityId(String universityId){
        return universityRepo.findById(universityId) != null;
    }

    public Department save(Department department){
        if(!validateName(department.getName())){
            throw new IllegalArgumentException("Department name cannot be null or blank");
        }

        if(!validateUniversityId(department.getUniversityId())){
            throw new IllegalArgumentException("University with id " + department.getUniversityId() + " not found");
        }

        // a re-save with a new object must not lose the existing courses and teachers
        Department existing = departmentRepo.findById(department.getId());
        if(existing != null){
            department.setCourses(existing.getCourses());
            department.setTeachers(existing.getTeachers());
        }

        departmentRepo.save(department);

        // unlink first, so a re-save neither duplicates the entry nor leaves it on the old university
        unlink(department.getId());
        universityRepo.findById(department.getUniversityId()).getDepartments().add(department);

        return department;
    }

    private void unlink(String id){
        for(University university : universityRepo.findAll()){
            university.getDepartments().removeIf(d -> d.getId().equals(id));
        }
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
        Department department = getById(id);

        // courses and teachers are real data, so they are not deleted with it and must not point to a missing department
        if(!department.getCourses().isEmpty() || !department.getTeachers().isEmpty()){
            throw new IllegalArgumentException("Department with id " + id + " still has courses or teachers");
        }

        departmentRepo.delete(id);
        unlink(id);
    }
}
