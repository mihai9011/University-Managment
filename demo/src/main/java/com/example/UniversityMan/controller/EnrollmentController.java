package com.example.UniversityMan.controller;

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

import com.example.UniversityMan.model.Enrollment;
import com.example.UniversityMan.service.EnrollmentService;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrolServ;

    EnrollmentController(EnrollmentService enrolServ){
        this.enrolServ = enrolServ;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments(){
        return enrolServ.getAll();
    }

    @PostMapping 
    public ResponseEntity<String> createOrUpdateEnrollments(@RequestBody Enrollment enrollment){
        try{
            enrolServ.save(enrollment);
            return ResponseEntity.ok("Enrollment added");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable String id){
        try{
            Enrollment enrollment = enrolServ.getById(id);
            return ResponseEntity.ok(enrollment);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            enrolServ.delete(id);
            return ResponseEntity.ok("Enrollment with id " + id + " deleted.");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
