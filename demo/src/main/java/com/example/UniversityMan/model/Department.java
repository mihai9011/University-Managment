package com.example.UniversityMan.model;

import java.util.ArrayList;
import java.util.List;

public class Department extends BaseEntity {
	private String name;
	private List<Course> courses = new ArrayList<>();
	private List<Teacher> teachers = new ArrayList<>();

	public Department() {}

	public Department(String name) {
		this.name = name;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public List<Course> getCourses() { return courses; }
	public void setCourses(List<Course> courses) { this.courses = courses; }
	public List<Teacher> getTeachers() { return teachers; }
	public void setTeachers(List<Teacher> teachers) { this.teachers = teachers; }
}
