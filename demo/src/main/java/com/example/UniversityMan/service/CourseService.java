package com.example.UniversityMan.service;

import org.springframework.stereotype.Service;
import com.example.UniversityMan.repository.CourseRepository;
import com.example.UniversityMan.repository.DepartmentRepository;
import com.example.UniversityMan.repository.RoomRepository;

@Service
public class CourseService {
    private final CourseRepository courseRepo;
    private final DepartmentRepository departmentRepo;
    private final RoomRepository roomRepo;

    CourseService(CourseRepository courseRepo, DepartmentRepository departmentRepo, RoomRepository roomRepo){
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
}
