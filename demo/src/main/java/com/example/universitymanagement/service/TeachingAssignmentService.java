package com.example.universitymanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Course;
import com.example.universitymanagement.model.Staff;
import com.example.universitymanagement.model.TeachingAssignment;
import com.example.universitymanagement.repository.Repo;

@Service
public class TeachingAssignmentService {
    // injecting repos to avoid circular dependencies
    private final Repo<Course> courseRepo;
    private final Repo<TeachingAssignment> taRepo;
    private final Repo<Staff> staffRepo;

    TeachingAssignmentService(Repo<Course> courseRepo, Repo<TeachingAssignment> taRepo, Repo<Staff> staffRepo) {
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

        // the same person can teach both the lab and the seminar of a course, so managing is part of the check
        for (TeachingAssignment other : taRepo.findAll()) {
            if (!other.getId().equals(ta.getId())
                    && other.getCourseId().equals(ta.getCourseId())
                    && other.getStaffId().equals(ta.getStaffId())
                    && other.getManaging() == ta.getManaging()) {
                throw new IllegalArgumentException("This teaching assignment already exists");
            }
        }

        taRepo.save(ta);

        // unlink first, so a re-save neither duplicates the entry nor leaves it on the old course/staff
        unlink(ta.getId());
        courseRepo.findById(ta.getCourseId()).getAssignments().add(ta);
        staffRepo.findById(ta.getStaffId()).getAssignments().add(ta);
    }

    private void unlink(String id) {
        for (Course course : courseRepo.findAll()) {
            course.getAssignments().removeIf(a -> a.getId().equals(id));
        }
        for (Staff staff : staffRepo.findAll()) {
            staff.getAssignments().removeIf(a -> a.getId().equals(id));
        }
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

        unlink(id);

        System.out.println("TeachingAssignment with id " + id + " deleted.");
    }
}