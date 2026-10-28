package com.example.UniversityMan.model;

public class Assistant extends Staff {
	private AssistantRole role;

	public Assistant() {}

	public Assistant(String name, AssistantRole role) {
		super(name);
		this.role = role;
	}

	public AssistantRole getRole() { return role; }
	public void setRole(AssistantRole role) { this.role = role; }
}
