package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.universitymanagement.model.StaffDTO;
import com.example.universitymanagement.model.Assistant;
import com.example.universitymanagement.model.Staff;
import com.example.universitymanagement.model.Teacher;
import com.example.universitymanagement.service.StaffService;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("staffList", staffService.getAll());
        return "staff/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("staffDto", new StaffDTO()); 
        return "staff/form";
    }

    @PostMapping
    public String save(@ModelAttribute StaffDTO staffDto) {
        Staff staff;
        
        //instantiating with type attribute
        if ("assistant".equalsIgnoreCase(staffDto.getType())) {
            staff = new Assistant();
        } else {
            staff = new Teacher();
        }
        
        //transfering data
        staff.setId(staffDto.getId());
        staff.setName(staffDto.getName());
        
        staffService.save(staff);
        
        return "redirect:/staff"; 
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        staffService.delete(id);
        return "redirect:/staff"; 
    }
}