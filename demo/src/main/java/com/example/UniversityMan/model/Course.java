package com.example.UniversityMan.model;

import java.util.ArrayList;
import java.util.List;

public class Course extends BaseEntity {
	private String title;
	private int credits;
	private String semester; // neue Eigenschaft (Aufgabe 5)
	private String departmentId;
	private String roomId;
	private List<Enrollment> enrollments = new ArrayList<>();
	private List<TeachingAssignment> assignments = new ArrayList<>();

	public Course() {}

	public Course(String title, int credits, String semester, String departmentId, String roomId) {
		this.title = title;
		this.credits = credits;
		this.semester = semester;
		this.departmentId = departmentId;
		this.roomId = roomId;
	}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public int getCredits() { return credits; }
	public void setCredits(int credits) { this.credits = credits; }
	public String getSemester() { return semester; }
	public void setSemester(String semester) { this.semester = semester; }
	public String getDepartmentId() { return departmentId; }
	public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
	public String getRoomId() { return roomId; }
	public void setRoomId(String roomId) { this.roomId = roomId; }
	public List<Enrollment> getEnrollments() { return enrollments; }
	public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
	public List<TeachingAssignment> getAssignments() { return assignments; }
	public void setAssignments(List<TeachingAssignment> assignments) { this.assignments = assignments; }
}
