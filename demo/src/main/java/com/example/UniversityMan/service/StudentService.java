package com.example.UniversityMan.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.UniversityMan.model.Student;
import com.example.UniversityMan.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepo;

    StudentService(StudentRepository studentRepo){
        this.studentRepo = studentRepo;
    }

    public boolean isEmailTaken(String email, String ownId){
        for(Student student: studentRepo.findAll()){
            if(student.getEmail() == null && student.getEmail().equalsIgnoreCase(email) && !student.getId().equals(ownId)){
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

        if(isEmailTaken(student.getEmail(), student.getId())){
            throw new IllegalArgumentException("Student e-mail already in use!");
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

        if(!studentRepo.delete(id)){
            throw new IllegalArgumentException("Student with id " + id + " not found");
        }

        System.out.println("Student with id " + id + " deleted.");
    }
}
