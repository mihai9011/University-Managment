package com.example.universitymanagement.model;

import java.time.LocalDate;

public class StaffDTO {
    private String name;
    private String type; // "teacher" or "assistant"
    
    // Teacher specific fields
    private String title;
    private String departmentId;
    private String email;
    private LocalDate hireDate;
    
    // Assistant specific fields (as String, to be parsed into Enum later)
    private String assistantRole;

    public StaffDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getAssistantRole() { return assistantRole; }
    public void setAssistantRole(String assistantRole) { this.assistantRole = assistantRole; }
}