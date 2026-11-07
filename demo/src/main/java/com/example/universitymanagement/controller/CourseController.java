package com.example.universitymanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.universitymanagement.model.Course;
import com.example.universitymanagement.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseServ;

    CourseController(CourseService courseServ){
        this.courseServ = courseServ;
    }

    @GetMapping
    public List<Course> getAllCourses(){
        return courseServ.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable String id){
        try{
            Course course = courseServ.getById(id);
            return ResponseEntity.ok(course);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateCourse(@RequestBody Course course){
        try{
            courseServ.save(course);
            return ResponseEntity.status(HttpStatus.CREATED).body("Course saved successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            courseServ.delete(id);
            return ResponseEntity.ok("Course with " + id + " deleted successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
