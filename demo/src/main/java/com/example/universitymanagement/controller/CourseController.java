package com.example.universitymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.universitymanagement.model.Course;
import com.example.universitymanagement.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseServ;

    CourseController(CourseService courseServ){
        this.courseServ = courseServ;
    }

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("courses", courseServ.getAll());
        return "course/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("course", new Course());
        return "course/form";
    }

    @PostMapping
    public String save(@ModelAttribute Course course){
        courseServ.save(course);
        return "redirect:/courses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id){
        courseServ.delete(id);
        return "redirect:/courses";
    }
}
