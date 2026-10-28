package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Course;
import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.model.Room;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends CrudService<Course> {
	private final GenericRepository<Department> departments;
	private final GenericRepository<Room> rooms;

	public CourseService(GenericRepository<Course> courses,
						 GenericRepository<Department> departments,
						 GenericRepository<Room> rooms) {
		super(courses);
		this.departments = departments;
		this.rooms = rooms;
	}

	@Override
	public Course create(Course course) {
		Department department = require(departments, course.getDepartmentId());
		Room room = course.getRoomId() == null ? null : require(rooms, course.getRoomId());
		Course saved = super.create(course);
		department.getCourses().add(saved);
		if (room != null) {
			room.getCourses().add(saved);
		}
		return saved;
	}

	@Override
	protected void beforeDelete(Course course) {
		ensureEmpty(course.getEnrollments(), "Einschreibungen");
		ensureEmpty(course.getAssignments(), "Lehrzuweisungen");
		require(departments, course.getDepartmentId()).getCourses().remove(course);
		if (course.getRoomId() != null) {
			require(rooms, course.getRoomId()).getCourses().remove(course);
		}
	}
}
