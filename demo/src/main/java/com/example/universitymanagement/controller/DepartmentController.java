package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.universitymanagement.model.Department;
import com.example.universitymanagement.service.DepartmentService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentServ;

    DepartmentController(DepartmentService departmentServ){
        this.departmentServ = departmentServ;
    }

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("departments", departmentServ.getAll());
        return "department/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("department", new Department());
        return "department/form";
    }

    @PostMapping
    public String save(@ModelAttribute Department department){
        departmentServ.save(department);
        return "redirect:/departments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id){
        departmentServ.delete(id);
        return "redirect:/departments";
    }
}
