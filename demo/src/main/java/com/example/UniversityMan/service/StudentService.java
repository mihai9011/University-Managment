package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Student;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends CrudService<Student> {

	public StudentService(GenericRepository<Student> students) {
		super(students);
	}

	@Override
	protected void beforeDelete(Student student) {
		ensureEmpty(student.getEnrollments(), "Einschreibungen");
	}
}
