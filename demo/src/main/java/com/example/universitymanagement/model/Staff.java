package com.example.universitymanagement.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Staff extends BaseEntity {
	private String name;
	private List<TeachingAssignment> assignments = new ArrayList<>();
	

	protected Staff() {}

	protected Staff(String name) {
		this.name = name;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public List<TeachingAssignment> getAssignments() { return assignments; }
	public void setAssignments(List<TeachingAssignment> assignments) { this.assignments = assignments; }
}
