package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Course;
import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.model.Enrollment;
import com.example.UniversityMan.model.Room;
import com.example.UniversityMan.model.Student;
import com.example.UniversityMan.repository.CourseRepository;
import com.example.UniversityMan.repository.DepartmentRepository;
import com.example.UniversityMan.repository.EnrollmentRepository;
import com.example.UniversityMan.repository.RoomRepository;
import com.example.UniversityMan.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnrollmentServiceTest {
	private final StudentRepository students = new StudentRepository();
	private final CourseRepository courses = new CourseRepository();
	private final DepartmentRepository departments = new DepartmentRepository();
	private final RoomRepository rooms = new RoomRepository();
	private final EnrollmentService enrollments =
			new EnrollmentService(new EnrollmentRepository(), students, courses, rooms);

	@Test
	void rejectsDuplicatesAndFullRooms() {
		Department department = departments.save(new Department("Informatik"));
		Room room = rooms.save(new Room(1, "A1"));
		Course course = new CourseService(courses, departments, rooms)
				.create(new Course("MAP", 5, "WS", department.getId(), room.getId()));
		Student first = students.save(new Student("Ana", "ana@example.com", null));
		Student second = students.save(new Student("Ion", "ion@example.com", null));

		enrollments.create(new Enrollment(first.getId(), course.getId()));
		assertEquals(1, course.getEnrollments().size());
		assertEquals(1, first.getEnrollments().size());

		assertThrows(IllegalStateException.class,
				() -> enrollments.create(new Enrollment(first.getId(), course.getId())));
		assertThrows(IllegalStateException.class,
				() -> enrollments.create(new Enrollment(second.getId(), course.getId())));
	}
}
