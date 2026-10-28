package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.Course;
import com.example.UniversityMan.service.CourseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CourseController extends CrudController<Course> {

	public CourseController(CourseService service) {
		super(service);
	}
}
