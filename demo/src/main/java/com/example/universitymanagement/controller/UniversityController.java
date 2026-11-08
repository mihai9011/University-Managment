package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.universitymanagement.model.University;
import com.example.universitymanagement.service.UniversityService;

@Controller
@RequestMapping("/universities")
public class UniversityController {

    private final UniversityService universityServ;

    UniversityController(UniversityService universityServ){
        this.universityServ = universityServ;
    }

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("universities", universityServ.getAll());
        return "university/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("university", new University());
        return "university/form";
    }

    @PostMapping
    public String save(@ModelAttribute University university){
        universityServ.save(university);
        return "redirect:/universities";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id){
        universityServ.delete(id);
        return "redirect:/universities";
    }
}
