package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Course;
import com.example.UniversityMan.model.Staff;
import com.example.UniversityMan.model.TeachingAssignment;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class TeachingAssignmentService extends CrudService<TeachingAssignment> {
	private final GenericRepository<Course> courses;
	private final GenericRepository<Staff> staff;

	public TeachingAssignmentService(GenericRepository<TeachingAssignment> assignments,
									 GenericRepository<Course> courses,
									 GenericRepository<Staff> staff) {
		super(assignments);
		this.courses = courses;
		this.staff = staff;
	}

	@Override
	public TeachingAssignment create(TeachingAssignment assignment) {
		Course course = require(courses, assignment.getCourseId());
		Staff member = require(staff, assignment.getStaffId());
		if (assignment.getManaging() == null) {
			throw new IllegalArgumentException("managing fehlt");
		}
		TeachingAssignment saved = super.create(assignment);
		course.getAssignments().add(saved);
		member.getAssignments().add(saved);
		return saved;
	}

	@Override
	protected void beforeDelete(TeachingAssignment assignment) {
		require(courses, assignment.getCourseId()).getAssignments().remove(assignment);
		require(staff, assignment.getStaffId()).getAssignments().remove(assignment);
	}
}
