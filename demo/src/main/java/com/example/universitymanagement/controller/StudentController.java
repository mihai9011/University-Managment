package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.universitymanagement.model.Student;
import com.example.universitymanagement.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "student/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student()); 
        return "student/form";
    }

    @PostMapping
    public String save(@ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/student"; 
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        studentService.delete(id);
        return "redirect:/student"; 
    }
}