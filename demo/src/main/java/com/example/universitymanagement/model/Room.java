package com.example.universitymanagement.model;

import java.util.ArrayList;
import java.util.List;

public class Room extends BaseEntity {
	private double capacity;
	private String number;
	private String universityId; // not in the uml, needed to keep University.rooms in sync
	private List<Course> courses = new ArrayList<>();

	public Room() {}

	public Room(double capacity, String number, String universityId) {
		this.capacity = capacity;
		this.number = number;
		this.universityId = universityId;
	}

	public double getCapacity() { return capacity; }
	public void setCapacity(double capacity) { this.capacity = capacity; }
	public String getNumber() { return number; }
	public void setNumber(String number) { this.number = number; }
	public String getUniversityId() { return universityId; }
	public void setUniversityId(String universityId) { this.universityId = universityId; }
	public List<Course> getCourses() { return courses; }
	public void setCourses(List<Course> courses) { this.courses = courses; }
}
