package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.Student;
import com.example.UniversityMan.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController extends CrudController<Student> {

	public StudentController(StudentService service) {
		super(service);
	}
}
