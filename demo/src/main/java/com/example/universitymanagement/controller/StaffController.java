package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.universitymanagement.model.StaffDTO;
import com.example.universitymanagement.model.Assistant;
import com.example.universitymanagement.model.AssistantRole;
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

    @GetMapping("/{id}")
    public String details(@PathVariable String id, Model model) {
        model.addAttribute("staff", staffService.getById(id));
        return "staff/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Staff staff = staffService.getById(id);
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setName(staff.getName());
        
        if (staff instanceof Teacher teacher) {
            dto.setType("teacher");
            dto.setTitle(teacher.getTitle());
            dto.setDepartmentId(teacher.getDepartmentId());
            dto.setEmail(teacher.getEmail());
            dto.setHireDate(teacher.getHireDate());
        } else if (staff instanceof Assistant assistant) {
            dto.setType("assistant");
            dto.setAssistantRole(assistant.getRole() != null ? assistant.getRole().name() : null);
        }
        
        model.addAttribute("staffDto", dto);
        return "staff/form";
    }

    @PostMapping
    public String save(@ModelAttribute StaffDTO staffDto) {
        if ("assistant".equalsIgnoreCase(staffDto.getType())) {
            Assistant assistant = new Assistant();

            //if its editing, keeping the old id
            if (staffDto.getId() != null && !staffDto.getId().isBlank()) {
                assistant.setId(staffDto.getId());
            }

            assistant.setName(staffDto.getName());
            assistant.setRole(AssistantRole.valueOf(staffDto.getAssistantRole())); 
        
            staffService.save(assistant);
        } else {
            Teacher teacher = new Teacher();

            // if its editing, keeping the old id
            if (staffDto.getId() != null && !staffDto.getId().isBlank()) {
                teacher.setId(staffDto.getId());
            }

            teacher.setName(staffDto.getName());
            teacher.setTitle(staffDto.getTitle());
            teacher.setDepartmentId(staffDto.getDepartmentId());
            teacher.setEmail(staffDto.getEmail());
            teacher.setHireDate(staffDto.getHireDate());
        
            staffService.save(teacher);
        }
        
        return "redirect:/staff"; 
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        staffService.delete(id);
        return "redirect:/staff"; 
    }
}