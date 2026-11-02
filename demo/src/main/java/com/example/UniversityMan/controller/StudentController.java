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

import com.example.UniversityMan.model.Student;
import com.example.UniversityMan.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studServ;

    StudentController( StudentService studServ){
        this.studServ = studServ;
    }

    @GetMapping 
    public List<Student> getAllStudents(){
        return studServ.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        try{
            Student student = studServ.getById(id);
            return ResponseEntity.ok(student);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateStudent(@RequestBody Student student){
        try{
            studServ.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student saved successfully!");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }   

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            studServ.delete(id);
            return ResponseEntity.ok("Student with " + id + " deleted successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
