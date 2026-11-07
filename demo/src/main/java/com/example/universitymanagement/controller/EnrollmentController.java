package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.universitymanagement.model.Enrollment;
import com.example.universitymanagement.service.EnrollmentService;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("enrollments", enrollmentService.getAll());
        return "enrollment/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("enrollment", new Enrollment()); 
        return "enrollment/form";
    }

    @PostMapping
    public String save(@ModelAttribute Enrollment enrollment) {
        enrollmentService.save(enrollment);
        return "redirect:/enrollment"; 
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        enrollmentService.delete(id);
        return "redirect:/enrollment"; 
    }
}