package com.example.UniversityMan.model;

public class TeachingAssignment extends BaseEntity {
	private String courseId;
	private String staffId;
	private ManagingType managing;

	public TeachingAssignment() {}

	public TeachingAssignment(String courseId, String staffId, ManagingType managing) {
		this.courseId = courseId;
		this.staffId = staffId;
		this.managing = managing;
	}

	public String getCourseId() { return courseId; }
	public void setCourseId(String courseId) { this.courseId = courseId; }
	public String getStaffId() { return staffId; }
	public void setStaffId(String staffId) { this.staffId = staffId; }
	public ManagingType getManaging() { return managing; }
	public void setManaging(ManagingType managing) { this.managing = managing; }
}
