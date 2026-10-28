package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.TeachingAssignment;
import com.example.UniversityMan.service.TeachingAssignmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assignments")
public class TeachingAssignmentController extends CrudController<TeachingAssignment> {

	public TeachingAssignmentController(TeachingAssignmentService service) {
		super(service);
	}
}
