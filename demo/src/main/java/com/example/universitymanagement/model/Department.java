package com.example.universitymanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Department extends BaseEntity {
	private String name;
	private String universityId; // not in the uml, needed to keep University.departments in sync
	private List<Course> courses = new ArrayList<>();
	private List<Teacher> teachers = new ArrayList<>();

	public Department() {}

	public Department(String name, String universityId) {
		this.name = name;
		this.universityId = universityId;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getUniversityId() { return universityId; }
	public void setUniversityId(String universityId) { this.universityId = universityId; }
	@JsonIgnore
	public List<Course> getCourses() { return courses; }
	public void setCourses(List<Course> courses) { this.courses = courses; }
	@JsonIgnore
	public List<Teacher> getTeachers() { return teachers; }
	public void setTeachers(List<Teacher> teachers) { this.teachers = teachers; }
}
