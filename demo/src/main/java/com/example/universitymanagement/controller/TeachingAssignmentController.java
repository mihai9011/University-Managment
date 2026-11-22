package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.universitymanagement.model.TeachingAssignment;
import com.example.universitymanagement.service.TeachingAssignmentService;

@Controller
@RequestMapping("/teachingassignments")
public class TeachingAssignmentController {

    private final TeachingAssignmentService teachingAssignmentService;

    public TeachingAssignmentController(TeachingAssignmentService teachingAssignmentService) {
        this.teachingAssignmentService = teachingAssignmentService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("assignments", teachingAssignmentService.getAll());
        return "teachingassignment/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("teachingAssignment", new TeachingAssignment()); 
        return "teachingassignment/form";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable String id, Model model){
        model.addAttribute("teachingAssignment", teachingAssignmentService.getById(id));
        return "teachingassignment/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model){
        model.addAttribute("teachingAssignment", teachingAssignmentService.getById(id));
        return "teachingassignment/form";
    }


    @PostMapping
    public String save(@ModelAttribute TeachingAssignment teachingAssignment) {
        teachingAssignmentService.save(teachingAssignment);
        return "redirect:/teachingassignments"; 
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        teachingAssignmentService.delete(id);
        return "redirect:/teachingassignments"; 
    }
}