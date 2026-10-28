package com.example.UniversityMan.model;

import java.util.ArrayList;
import java.util.List;

public class University extends BaseEntity {
	private String name;
	private String city;
	private List<Department> departments = new ArrayList<>();
	private List<Room> rooms = new ArrayList<>();

	public University() {}

	public University(String name, String city) {
		this.name = name;
		this.city = city;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
	public List<Department> getDepartments() { return departments; }
	public void setDepartments(List<Department> departments) { this.departments = departments; }
	public List<Room> getRooms() { return rooms; }
	public void setRooms(List<Room> rooms) { this.rooms = rooms; }
}
