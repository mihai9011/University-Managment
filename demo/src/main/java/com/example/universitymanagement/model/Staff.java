package com.example.universitymanagement.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Teacher.class, name = "teacher"),
    @JsonSubTypes.Type(value = Assistant.class, name = "assistant")
})

public abstract class Staff extends BaseEntity {
	private String name;
	private List<TeachingAssignment> assignments = new ArrayList<>();
	

	protected Staff() {}

	protected Staff(String name) {
		this.name = name;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	@JsonIgnore
	public List<TeachingAssignment> getAssignments() { return assignments; }
	public void setAssignments(List<TeachingAssignment> assignments) { this.assignments = assignments; }
}
