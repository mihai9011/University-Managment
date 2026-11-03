package com.example.UniversityMan.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.UniversityMan.model.Assistant;
import com.example.UniversityMan.model.Staff;
import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.model.Teacher;
import com.example.UniversityMan.model.TeachingAssignment;

import com.example.UniversityMan.repository.Repo;


@Service
public class StaffService implements ValidatableService{
    private final Repo<Staff> staffRepo;
    private final Repo<Department>  depRepo;
    private final Repo<TeachingAssignment> taRepo;

    public StaffService(Repo<Staff> staffRepo, Repo<Department> depRepo, Repo<TeachingAssignment> taRepo){
        this.staffRepo = staffRepo;
        this.depRepo = depRepo;
        this.taRepo = taRepo;
    }

    public boolean isEmailTaken(String email, String ownId){
        if (email == null) return false;

        for(Staff staff: staffRepo.findAll()){
            if(staff instanceof Teacher teacher){
                if(teacher.getEmail() != null && teacher.getEmail().equalsIgnoreCase(email) && !staff.getId().equals(ownId)){
                    return true;
                }
            }
        }
        return false;
    }

    public void checkAssignments(Staff staff){
       if (staff.getAssignments() == null || staff.getAssignments().isEmpty()) {
            return;
       }

        for(TeachingAssignment ta : staff.getAssignments()){
    
            TeachingAssignment existingTa = taRepo.findById(ta.getId());
            
            if(existingTa == null){
                throw new IllegalArgumentException("Teaching assignment with id " + ta.getId() + " doesn`t exist");
            }

            if(!Objects.equals(ta.getStaffId(), staff.getId())){
                throw new IllegalArgumentException("Assignments Staff id doesnt match the staffs id");
            }
            
            if(!Objects.equals(ta.getCourseId(), existingTa.getCourseId())){
                throw new IllegalArgumentException("Invalid Course id for assignment with id " + ta.getId());
            }
         
            if(!Objects.equals(ta.getManaging(), existingTa.getManaging())){
                throw new IllegalArgumentException("Invalid Managing type for assignment with id " + ta.getId());
            }
        }
    }

    public void save(Staff staff){
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null!");
        }   

        checkAssignments(staff);

        if(staff instanceof Assistant assistant){
            if(assistant.getRole() == null){
                throw new IllegalArgumentException("Assistant must have a role!");
            }

            staffRepo.save(assistant);
            return;
        }

        Teacher teacher = (Teacher) staff;

        if(teacher.getEmail() == null  || teacher.getEmail().isBlank()){
            throw new IllegalArgumentException("Staff member must have an e-mail!");
        }

        if(teacher.getTitle() == null){
            throw new IllegalArgumentException("Teacher must have a title!");
        }

        if(teacher.getDepartmentId() == null){
            throw new IllegalArgumentException("Teacher must have a Department Id!");
        }

        if(teacher.getHireDate() == null){
            throw new IllegalArgumentException("Teacher must have a Hire date!");
        }

        if(teacher.getName() == null){
            throw new IllegalArgumentException("Teacher must have a name!");
        }
        
        if (!isValidEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("Invalid E-mail format!");
        }

        if(isEmailTaken(teacher.getEmail() ,teacher.getId())){
            throw new IllegalArgumentException("E-mail taken!");
        }

        if(depRepo.findById(teacher.getDepartmentId()) == null){
            throw new IllegalArgumentException("Department with id " + teacher.getDepartmentId() + " doesn`t exist!");
        }
        
        staffRepo.save(teacher);
    }

    public List<Staff> getAll(){
        return staffRepo.findAll();
    }

    public Staff getById(String id){
        Staff s = staffRepo.findById(id);

        if(s == null){
            throw new IllegalArgumentException("Staff with id " + id + " not found");
        }

        return s;
    }

    public void delete(String id){
        if(!staffRepo.delete(id)){
            throw new IllegalArgumentException("Staff with id " + id + " not found");
        }

        System.out.println("Staff with id " + id + " deleted.");
    }
}