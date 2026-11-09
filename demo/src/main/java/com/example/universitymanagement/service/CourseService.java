package com.example.universitymanagement.service;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Course;
import com.example.universitymanagement.model.Department;
import com.example.universitymanagement.model.Enrollment;
import com.example.universitymanagement.model.Room;
import com.example.universitymanagement.model.TeachingAssignment;
import com.example.universitymanagement.repository.Repo;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final Repo<Course> courseRepo;
    private final Repo<Department> departmentRepo;
    private final Repo<Room> roomRepo;
    private final EnrollmentService enrollmentService;
    private final TeachingAssignmentService assignmentService;

    CourseService(Repo<Course> courseRepo, Repo<Department> departmentRepo, Repo<Room> roomRepo,
                  EnrollmentService enrollmentService, TeachingAssignmentService assignmentService){
        this.courseRepo = courseRepo;
        this.departmentRepo = departmentRepo;
        this.roomRepo = roomRepo;
        this.enrollmentService = enrollmentService;
        this.assignmentService = assignmentService;
    }

    public boolean validateDepartmentId(String departmentId){
        return departmentRepo.findById(departmentId) != null;
    }

    public boolean validateRoomId(String roomId){
        return roomRepo.findById(roomId) != null;
    }

    public Course save(Course course){
        if(!validateDepartmentId(course.getDepartmentId())){
            throw new IllegalArgumentException("Department with id " + course.getDepartmentId() + " not found");
        }

        if(!validateRoomId(course.getRoomId())){
            throw new IllegalArgumentException("Room with id " + course.getRoomId() + " not found");
        }

        // a re-save with a new object must not lose what is already linked to this course
        Course existing = courseRepo.findById(course.getId());
        if(existing != null){
            course.setEnrollments(existing.getEnrollments());
            course.setAssignments(existing.getAssignments());
        }

        courseRepo.save(course);

        // unlink first, so a re-save neither duplicates the entry nor leaves it on the old department/room
        unlink(course.getId());
        departmentRepo.findById(course.getDepartmentId()).getCourses().add(course);
        roomRepo.findById(course.getRoomId()).getCourses().add(course);

        return course;
    }

    private void unlink(String id){
        for(Department department : departmentRepo.findAll()){
            department.getCourses().removeIf(c -> c.getId().equals(id));
        }
        for(Room room : roomRepo.findAll()){
            room.getCourses().removeIf(c -> c.getId().equals(id));
        }
    }

    public List<Course> getAll(){
        return courseRepo.findAll();
    }

    public Course getById(String id){
        Course course = courseRepo.findById(id);

        if(course == null){
            throw new IllegalArgumentException("Course with id " + id + " not found");
        }

        return course;
    }

    public void delete(String id){
        Course course = getById(id);

        // an enrollment or assignment makes no sense without its course. iterate over copies, delete changes the lists
        for(Enrollment enrollment : new ArrayList<>(course.getEnrollments())){
            enrollmentService.delete(enrollment.getId());
        }
        for(TeachingAssignment assignment : new ArrayList<>(course.getAssignments())){
            assignmentService.delete(assignment.getId());
        }

        courseRepo.delete(id);
        unlink(id);
    }
}
