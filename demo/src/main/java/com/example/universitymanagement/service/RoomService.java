package com.example.universitymanagement.service;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Room;
import com.example.universitymanagement.repository.Repo;

import java.util.List;

@Service
public class RoomService {
    private final Repo<Room> roomRepo;

    RoomService(Repo<Room> roomRepo){
        this.roomRepo = roomRepo;
    }

    public boolean validateCapacity(double capacity){
        return capacity > 0;
    }

    public Room save(Room room){
        if(!validateCapacity(room.getCapacity())){
            throw new IllegalArgumentException("Room capacity must be greater than 0");
        }

        // a re-save with a new object must not lose the existing courses
        Room existing = roomRepo.findById(room.getId());
        if(existing != null){
            room.setCourses(existing.getCourses());
        }

        return roomRepo.save(room);
    }

    public List<Room> getAll(){
        return roomRepo.findAll();
    }

    public Room getById(String id){
        Room room = roomRepo.findById(id);

        if(room == null){
            throw new IllegalArgumentException("Room with id " + id + " not found");
        }

        return room;
    }

    public void delete(String id){
        if(!roomRepo.delete(id)){
            throw new IllegalArgumentException("Room with id " + id + " not found");
        }
    }
}
