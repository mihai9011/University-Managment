package com.example.universitymanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.universitymanagement.model.Room;
import com.example.universitymanagement.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomServ;

    RoomController(RoomService roomServ){
        this.roomServ = roomServ;
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomServ.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable String id){
        try{
            Room room = roomServ.getById(id);
            return ResponseEntity.ok(room);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateRoom(@RequestBody Room room){
        try{
            roomServ.save(room);
            return ResponseEntity.status(HttpStatus.CREATED).body("Room saved successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable String id){
        try{
            roomServ.delete(id);
            return ResponseEntity.ok("Room with " + id + " deleted successfully!");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
