package com.example.UniversityMan.service;

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

    public boolean validateEmail(String email){
        List<Student> students = studentRepo.findAll();

        if(students.size() == 0)return true;

        for(Student student: students){
            if(student.getEmail().equals(email)){
                return false;
            }
        }

        return true;
    }

    public void save(Student student){
        if(student.getEmail().contains(" ")){
            throw new IllegalArgumentException("E-mail contains spaces!");
        }
        
        if(validateEmail(student.getEmail())){
            studentRepo.save(student);
            System.out.println("Student added.");
        }
        else{
            System.out.println("Student e-mail already in use!");
        }    
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
