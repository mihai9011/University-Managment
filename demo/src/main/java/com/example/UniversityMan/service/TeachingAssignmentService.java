package com.example.UniversityMan.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.UniversityMan.model.TeachingAssignment;
import com.example.UniversityMan.repository.CourseRepository;
import com.example.UniversityMan.repository.StaffRepository;
import com.example.UniversityMan.repository.TeachingAssignmentRepository;

@Service
public class TeachingAssignmentService {
    // injecting repos to avoid circular dependencies
    private final CourseRepository courseRepo;
    private final TeachingAssignmentRepository taRepo;
    private final StaffRepository staffRepo;

    TeachingAssignmentService(CourseRepository courseRepo, TeachingAssignmentRepository taRepo, StaffRepository staffRepo) {
        this.courseRepo = courseRepo;
        this.taRepo = taRepo;
        this.staffRepo = staffRepo;
    }

    public void save(TeachingAssignment ta) {
        if (ta == null) {
            throw new IllegalArgumentException("TeachingAssignment cannot be null!");
        }

        if (ta.getCourseId() == null) {
            throw new IllegalArgumentException("Course Id cannot be null!");
        }

        if (courseRepo.findById(ta.getCourseId()) == null) {
             throw new IllegalArgumentException("Course Id does not exist!");
        }

        if (ta.getStaffId() == null) {
            throw new IllegalArgumentException("Staff Id cannot be null!");
        }

        if (staffRepo.findById(ta.getStaffId()) == null) {
            throw new IllegalArgumentException("Staff Id does not exist!");
        }

        if (ta.getManaging() == null) {
            throw new IllegalArgumentException("Managing type left empty!");
        }

        taRepo.save(ta);
    }

    public List<TeachingAssignment> getAll() {
        return taRepo.findAll();
    }

    public TeachingAssignment getById(String id) {
        TeachingAssignment ta = taRepo.findById(id);

        if (ta == null) {
            throw new IllegalArgumentException("TeachingAssignment with id " + id + " not found");
        }

        return ta;
    }

    public void delete(String id) {
        if (!taRepo.delete(id)) {
            throw new IllegalArgumentException("TeachingAssignment with id " + id + " not found");
        }

        System.out.println("TeachingAssignment with id " + id + " deleted.");
    }
}