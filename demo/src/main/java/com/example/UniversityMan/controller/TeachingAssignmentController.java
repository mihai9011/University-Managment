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


import com.example.UniversityMan.model.TeachingAssignment;
import com.example.UniversityMan.service.TeachingAssignmentService;

@RestController
@RequestMapping("/assignments")
public class TeachingAssignmentController{
    private final TeachingAssignmentService taServ;
    
    TeachingAssignmentController(TeachingAssignmentService taServ){
        this.taServ = taServ;
    }

    @GetMapping
    public List<TeachingAssignment> getAllTeachingAssignments(){
        return taServ.getAll();
    }

    @PostMapping 
    public ResponseEntity<String> createOrUpdateTeachingAssignments(@RequestBody TeachingAssignment ta){
        try{
            taServ.save(ta);
            return ResponseEntity.status(HttpStatus.CREATED).body("Assignment added");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeachingAssignmentById(@PathVariable String id){
        try{
            TeachingAssignment ta = taServ.getById(id);
            return ResponseEntity.ok(ta);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            taServ.delete(id);
            return ResponseEntity.ok("Assignment with id " + id + " deleted.");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
