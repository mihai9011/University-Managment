package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.service.DepartmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
public class DepartmentController extends CrudController<Department> {

	public DepartmentController(DepartmentService service) {
		super(service);
	}
}
