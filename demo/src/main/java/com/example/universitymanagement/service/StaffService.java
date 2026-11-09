package com.example.universitymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Assistant;
import com.example.universitymanagement.model.Department;
import com.example.universitymanagement.model.Staff;
import com.example.universitymanagement.model.Teacher;
import com.example.universitymanagement.model.TeachingAssignment;
import com.example.universitymanagement.repository.Repo;


@Service
public class StaffService implements ValidatableService{
    private final Repo<Staff> staffRepo;
    private final Repo<Department>  depRepo;
    private final Repo<TeachingAssignment> taRepo;
    private final TeachingAssignmentService assignmentService;

    public StaffService(Repo<Staff> staffRepo, Repo<Department> depRepo, Repo<TeachingAssignment> taRepo,
                        TeachingAssignmentService assignmentService){
        this.staffRepo = staffRepo;
        this.depRepo = depRepo;
        this.taRepo = taRepo;
        this.assignmentService = assignmentService;
    }

    private void unlinkFromDepartments(String id){
        for(Department department : depRepo.findAll()){
            department.getTeachers().removeIf(t -> t.getId().equals(id));
        }
    }

    // a re-save with a new object must not lose the existing assignments
    private void keepAssignments(Staff staff){
        Staff existing = staffRepo.findById(staff.getId());
        if(existing != null){
            staff.setAssignments(existing.getAssignments());
        }
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

            keepAssignments(assistant);
            staffRepo.save(assistant);
            unlinkFromDepartments(assistant.getId());
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
        
        keepAssignments(teacher);
        staffRepo.save(teacher);

        // unlink first, so a re-save neither duplicates the entry nor leaves it on the old department
        unlinkFromDepartments(teacher.getId());
        depRepo.findById(teacher.getDepartmentId()).getTeachers().add(teacher);
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
        // an assignment makes no sense without its staff member. iterate over a copy, delete changes the list
        for(TeachingAssignment assignment : new ArrayList<>(getById(id).getAssignments())){
            assignmentService.delete(assignment.getId());
        }

        staffRepo.delete(id);
        unlinkFromDepartments(id);

        System.out.println("Staff with id " + id + " deleted.");
    }
}