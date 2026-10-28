package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Room;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService extends CrudService<Room> {

	public RoomService(GenericRepository<Room> rooms) {
		super(rooms);
	}

	@Override
	protected void beforeDelete(Room room) {
		ensureEmpty(room.getCourses(), "Kurse");
	}
}
