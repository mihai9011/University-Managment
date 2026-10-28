package com.example.UniversityMan.model;

public class Enrollment extends BaseEntity {
	private String studentId;
	private String courseId;
	private Grade grade = Grade.NA;

	public Enrollment() {}

	public Enrollment(String studentId, String courseId) {
		this.studentId = studentId;
		this.courseId = courseId;
	}

	public String getStudentId() { return studentId; }
	public void setStudentId(String studentId) { this.studentId = studentId; }
	public String getCourseId() { return courseId; }
	public void setCourseId(String courseId) { this.courseId = courseId; }
	public Grade getGrade() { return grade; }
	public void setGrade(Grade grade) { this.grade = grade; }
}
