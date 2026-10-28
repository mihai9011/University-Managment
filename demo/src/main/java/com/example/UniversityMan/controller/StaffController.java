package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.Staff;
import com.example.UniversityMan.service.StaffService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffController extends CrudController<Staff> {

	public StaffController(StaffService service) {
		super(service);
	}
}
