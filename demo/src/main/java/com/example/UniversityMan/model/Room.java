package com.example.UniversityMan.model;

import java.util.ArrayList;
import java.util.List;

public class Room extends BaseEntity {
	private double capacity;
	private String number;
	private List<Course> courses = new ArrayList<>();

	public Room() {}

	public Room(double capacity, String number) {
		this.capacity = capacity;
		this.number = number;
	}

	public double getCapacity() { return capacity; }
	public void setCapacity(double capacity) { this.capacity = capacity; }
	public String getNumber() { return number; }
	public void setNumber(String number) { this.number = number; }
	public List<Course> getCourses() { return courses; }
	public void setCourses(List<Course> courses) { this.courses = courses; }
}
