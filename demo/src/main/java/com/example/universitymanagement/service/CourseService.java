package com.example.universitymanagement.service;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Course;
import com.example.universitymanagement.model.Department;
import com.example.universitymanagement.model.Room;
import com.example.universitymanagement.repository.Repo;

import java.util.List;

@Service
public class CourseService {
    private final Repo<Course> courseRepo;
    private final Repo<Department> departmentRepo;
    private final Repo<Room> roomRepo;

    CourseService(Repo<Course> courseRepo, Repo<Department> departmentRepo, Repo<Room> roomRepo){
        this.courseRepo = courseRepo;
        this.departmentRepo = departmentRepo;
        this.roomRepo = roomRepo;
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

        return courseRepo.save(course);
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
        if(!courseRepo.delete(id)){
            throw new IllegalArgumentException("Course with id " + id + " not found");
        }
    }
}
