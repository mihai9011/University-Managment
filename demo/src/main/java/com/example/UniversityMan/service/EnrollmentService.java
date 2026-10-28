package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Course;
import com.example.UniversityMan.model.Enrollment;
import com.example.UniversityMan.model.Grade;
import com.example.UniversityMan.model.Room;
import com.example.UniversityMan.model.Student;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService extends CrudService<Enrollment> {
	private final GenericRepository<Student> students;
	private final GenericRepository<Course> courses;
	private final GenericRepository<Room> rooms;

	public EnrollmentService(GenericRepository<Enrollment> enrollments,
							 GenericRepository<Student> students,
							 GenericRepository<Course> courses,
							 GenericRepository<Room> rooms) {
		super(enrollments);
		this.students = students;
		this.courses = courses;
		this.rooms = rooms;
	}

	@Override
	public Enrollment create(Enrollment enrollment) {
		Student student = require(students, enrollment.getStudentId());
		Course course = require(courses, enrollment.getCourseId());
		boolean alreadyEnrolled = course.getEnrollments().stream()
				.anyMatch(e -> e.getStudentId().equals(student.getId()));
		if (alreadyEnrolled) {
			throw new IllegalStateException("Student ist bereits in diesem Kurs eingeschrieben");
		}
		if (course.getRoomId() != null
				&& course.getEnrollments().size() >= require(rooms, course.getRoomId()).getCapacity()) {
			throw new IllegalStateException("Der Raum des Kurses ist voll");
		}
		enrollment.setGrade(Grade.NA);
		Enrollment saved = super.create(enrollment);
		student.getEnrollments().add(saved);
		course.getEnrollments().add(saved);
		return saved;
	}

	public Enrollment assignGrade(String id, Grade grade) {
		Enrollment enrollment = findById(id);
		enrollment.setGrade(grade);
		return enrollment;
	}

	@Override
	protected void beforeDelete(Enrollment enrollment) {
		require(students, enrollment.getStudentId()).getEnrollments().remove(enrollment);
		require(courses, enrollment.getCourseId()).getEnrollments().remove(enrollment);
	}
}
