package com.example.UniversityMan.model;

import java.time.LocalDate;

public class Teacher extends Staff {
	private String title;
	private String departmentId;
	private String email; // neue Eigenschaft (Aufgabe 5)
	private LocalDate hireDate; // neue Eigenschaft (Aufgabe 5)

	public Teacher() {}

	public Teacher(String name, String title, String departmentId, String email, LocalDate hireDate) {
		super(name);
		this.title = title;
		this.departmentId = departmentId;
		this.email = email;
		this.hireDate = hireDate;
	}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDepartmentId() { return departmentId; }
	public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public LocalDate getHireDate() { return hireDate; }
	public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
}
