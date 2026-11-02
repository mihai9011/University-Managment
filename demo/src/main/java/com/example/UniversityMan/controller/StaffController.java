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

import com.example.UniversityMan.model.Staff;
import com.example.UniversityMan.service.StaffService;

@RestController
@RequestMapping("/staff")
public class StaffController {
    private final StaffService staffServ;

    StaffController(StaffService staffServ){
        this.staffServ = staffServ;
    }

    @GetMapping 
    public List<Staff> getAllStaff(){
        return staffServ.getAll();
    }

    @PostMapping 
    public ResponseEntity<String> createOrUpdateStaff(@RequestBody Staff staff){
        try{
            staffServ.save(staff);
            return ResponseEntity.status(HttpStatus.CREATED).body("Staff member saved successfully!");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }  

    @GetMapping("/{id}")
    public ResponseEntity<?>getStaffById(@PathVariable String id){
        try{
            Staff staff =  staffServ.getById(id);
            return ResponseEntity.ok(staff);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            staffServ.delete(id);
            return ResponseEntity.ok("Staff member with " + id + " deleted successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
