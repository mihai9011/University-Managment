package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.Enrollment;
import com.example.UniversityMan.model.Grade;
import com.example.UniversityMan.service.EnrollmentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController extends CrudController<Enrollment> {
	private final EnrollmentService service;

	public EnrollmentController(EnrollmentService service) {
		super(service);
		this.service = service;
	}

	@PutMapping("/{id}/grade")
	public Enrollment assignGrade(@PathVariable String id, @RequestParam Grade grade) {
		return service.assignGrade(id, grade);
	}
}
