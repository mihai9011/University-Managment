package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends CrudService<Department> {

	public DepartmentService(GenericRepository<Department> departments) {
		super(departments);
	}

	@Override
	protected void beforeDelete(Department department) {
		ensureEmpty(department.getCourses(), "Kurse");
		ensureEmpty(department.getTeachers(), "Lehrende");
	}
}
