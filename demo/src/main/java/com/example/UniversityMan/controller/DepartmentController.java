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

import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentServ;

    DepartmentController(DepartmentService departmentServ){
        this.departmentServ = departmentServ;
    }

    @GetMapping
    public List<Department> getAllDepartments(){
        return departmentServ.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable String id){
        try{
            Department department = departmentServ.getById(id);
            return ResponseEntity.ok(department);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateDepartment(@RequestBody Department department){
        try{
            departmentServ.save(department);
            return ResponseEntity.status(HttpStatus.CREATED).body("Department saved successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            departmentServ.delete(id);
            return ResponseEntity.ok("Department with " + id + " deleted successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
