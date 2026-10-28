package com.example.UniversityMan.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends BaseEntity {
	private String name;
	private String email; // neue Eigenschaft (Aufgabe 5)
	private LocalDate birthDate; // neue Eigenschaft (Aufgabe 5)
	private List<Enrollment> enrollments = new ArrayList<>();

	public Student() {}

	public Student(String name, String email, LocalDate birthDate) {
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public LocalDate getBirthDate() { return birthDate; }
	public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
	public List<Enrollment> getEnrollments() { return enrollments; }
	public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}
