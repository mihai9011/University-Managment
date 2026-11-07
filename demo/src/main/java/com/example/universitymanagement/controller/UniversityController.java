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

import com.example.universitymanagement.model.University;
import com.example.universitymanagement.service.UniversityService;

@RestController
@RequestMapping("/universities")
public class UniversityController {

    private final UniversityService universityServ;

    UniversityController(UniversityService universityServ){
        this.universityServ = universityServ;
    }

    @GetMapping
    public List<University> getAllUniversities(){
        return universityServ.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUniversityById(@PathVariable String id){
        try{
            University university = universityServ.getById(id);
            return ResponseEntity.ok(university);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateUniversity(@RequestBody University university){
        try{
            universityServ.save(university);
            return ResponseEntity.status(HttpStatus.CREATED).body("University saved successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            universityServ.delete(id);
            return ResponseEntity.ok("University with " + id + " deleted successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
