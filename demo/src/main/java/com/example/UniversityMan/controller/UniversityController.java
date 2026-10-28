package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.University;
import com.example.UniversityMan.service.UniversityService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/universities")
public class UniversityController extends CrudController<University> {
	private final UniversityService service;

	public UniversityController(UniversityService service) {
		super(service);
		this.service = service;
	}

	@PostMapping("/{id}/departments/{departmentId}")
	public University addDepartment(@PathVariable String id, @PathVariable String departmentId) {
		return service.addDepartment(id, departmentId);
	}

	@PostMapping("/{id}/rooms/{roomId}")
	public University addRoom(@PathVariable String id, @PathVariable String roomId) {
		return service.addRoom(id, roomId);
	}
}
