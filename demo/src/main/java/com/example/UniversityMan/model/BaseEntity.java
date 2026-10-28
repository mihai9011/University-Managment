package com.example.UniversityMan.model;

import java.util.UUID;

public abstract class BaseEntity {
	private String id = UUID.randomUUID().toString();


	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
}
