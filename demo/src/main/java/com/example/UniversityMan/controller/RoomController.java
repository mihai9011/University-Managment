package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.Room;
import com.example.UniversityMan.service.RoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController extends CrudController<Room> {

	public RoomController(RoomService service) {
		super(service);
	}
}
