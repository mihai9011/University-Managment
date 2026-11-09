package com.example.universitymanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Enrollment;
import com.example.universitymanagement.model.Student;
import com.example.universitymanagement.repository.Repo;

@Service
public class StudentService implements ValidatableService{
    private final Repo<Student> studentRepo;
    private final EnrollmentService enrollmentService;

    StudentService(Repo<Student> studentRepo, EnrollmentService enrollmentService){
        this.studentRepo = studentRepo;
        this.enrollmentService = enrollmentService;
    }

    public boolean isEmailTaken(String email, String ownId){
        for(Student student: studentRepo.findAll()){
            if(student.getEmail() != null && student.getEmail().equalsIgnoreCase(email) && !student.getId().equals(ownId)){
                return true;
            }
        }
        return false;
    }

    public void save(Student student){
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null!");
        }

        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Student must have a name!");
        }

        if (student.getEmail() == null || student.getEmail().isBlank()) {
            throw new IllegalArgumentException("Student must have an e-mail!");
        }

        if(student.getBirthDate() == null || student.getBirthDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birth date must be in the past!");
        }

        if (!isValidEmail(student.getEmail())) {
            throw new IllegalArgumentException("Invalid E-mail format!");
        }

        if(isEmailTaken(student.getEmail(), student.getId())){
            throw new IllegalArgumentException("Student e-mail already in use!");
        }

        // a re-save with a new object must not lose the existing enrollments
        Student existing = studentRepo.findById(student.getId());
        if(existing != null){
            student.setEnrollments(existing.getEnrollments());
        }

        studentRepo.save(student);
    }

    public List<Student> getAll(){
        return studentRepo.findAll();
    }

    public Student getById(String id){
        Student s = studentRepo.findById(id);

        if(s == null){
            throw new IllegalArgumentException("Student with id " + id + " not found");
        }

        return s;
    }

    public void delete(String id){
        // an enrollment makes no sense without its student. iterate over a copy, delete changes the list
        for(Enrollment enrollment : new ArrayList<>(getById(id).getEnrollments())){
            enrollmentService.delete(enrollment.getId());
        }

        studentRepo.delete(id);

        System.out.println("Student with id " + id + " deleted.");
    }
}
