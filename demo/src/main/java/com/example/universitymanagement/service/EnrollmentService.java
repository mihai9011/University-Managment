package com.example.universitymanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Course;
import com.example.universitymanagement.model.Enrollment;
import com.example.universitymanagement.model.Student;
import com.example.universitymanagement.repository.Repo;


@Service
public class EnrollmentService {
    private final Repo<Enrollment> enrollmentRepo;
    private final Repo<Student> studentRepo;
    private final Repo<Course> courseRepo;
    
    public EnrollmentService(Repo<Enrollment> enrollmentRepo, Repo<Student> studentRepo, Repo<Course> courseRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    public void save(Enrollment enrol) {
        if (enrol == null) {
            throw new IllegalArgumentException("Enrollment cannot be null!");
        }

        if (enrol.getStudentId() == null) {
            throw new IllegalArgumentException("Student Id cannot be null");
        }
        if (studentRepo.findById(enrol.getStudentId()) == null) {
            throw new IllegalArgumentException("Student Id from enrollment doesn't exist");
        }

        if (enrol.getCourseId() == null) {
            throw new IllegalArgumentException("Course Id cannot be null");
        }
        if (courseRepo.findById(enrol.getCourseId()) == null) {
            throw new IllegalArgumentException("Course Id from enrollment doesn't exist");
        }

        if (enrol.getGrade() == null) {
             throw new IllegalArgumentException("Grade for enrollment cannot be null");
        }

        enrollmentRepo.save(enrol);

        Student student = studentRepo.findById(enrol.getStudentId());
        if(student != null){
            student.getEnrollments().add(enrol);
        }

        Course course = courseRepo.findById(enrol.getCourseId());
        if(course != null){
            course.getEnrollments().add(enrol);
        }
    }

    public List<Enrollment> getAll() {
        return enrollmentRepo.findAll();
    }

    public Enrollment getById(String id) {
        Enrollment e = enrollmentRepo.findById(id);

        if (e == null) {
            throw new IllegalArgumentException("Enrollment with id " + id + " not found");
        }

        return e;
    }
    
    public void delete(String id) {
        Enrollment enrol = enrollmentRepo.findById(id);

        if (enrol == null) {
            throw new IllegalArgumentException("Enrollment with id " + id + " not found");
        }

        enrollmentRepo.delete(id);

        Student student = studentRepo.findById(enrol.getStudentId());
        if(student != null){
            List<Enrollment> newEnrollments = new ArrayList<>();
            for(Enrollment e : student.getEnrollments()){
                if(!e.getId().equals(id)){
                    newEnrollments.add(e);
                }
            }
            student.setEnrollments(newEnrollments);
        }

        Course course = courseRepo.findById(enrol.getCourseId());
        if(course != null){
            List<Enrollment> newEnrollments = new ArrayList<>();
            for(Enrollment e : course.getEnrollments()){
                if(!e.getId().equals(id)){
                    newEnrollments.add(e);
                }
            }
            course.setEnrollments(newEnrollments);
        }
    
        System.out.println("Enrollment with id " + id + " deleted.");
    }
}