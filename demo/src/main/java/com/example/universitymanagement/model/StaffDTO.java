package com.example.universitymanagement.model;

public class StaffDTO {
    private String name;
    private String type;//stores the type of staff for clea datatransfer

    public StaffDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}