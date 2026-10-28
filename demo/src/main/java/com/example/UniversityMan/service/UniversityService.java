package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.model.Room;
import com.example.UniversityMan.model.University;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class UniversityService extends CrudService<University> {
	private final GenericRepository<Department> departments;
	private final GenericRepository<Room> rooms;

	public UniversityService(GenericRepository<University> universities,
							 GenericRepository<Department> departments,
							 GenericRepository<Room> rooms) {
		super(universities);
		this.departments = departments;
		this.rooms = rooms;
	}

	public University addDepartment(String universityId, String departmentId) {
		University university = findById(universityId);
		Department department = require(departments, departmentId);
		if (!university.getDepartments().contains(department)) {
			university.getDepartments().add(department);
		}
		return university;
	}

	public University addRoom(String universityId, String roomId) {
		University university = findById(universityId);
		Room room = require(rooms, roomId);
		if (!university.getRooms().contains(room)) {
			university.getRooms().add(room);
		}
		return university;
	}
}
