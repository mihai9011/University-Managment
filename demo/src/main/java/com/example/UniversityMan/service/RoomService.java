package com.example.UniversityMan.service;

import org.springframework.stereotype.Service;
import com.example.UniversityMan.repository.RoomRepository;
import java.util.List;
import com.example.UniversityMan.model.Room;

@Service
public class RoomService {
    private final RoomRepository roomRepo;

    RoomService(RoomRepository roomRepo){
        this.roomRepo = roomRepo;
    }

    public boolean validateCapacity(double capacity){
        return capacity > 0;
    }

    public Room save(Room room){
        if(!validateCapacity(room.getCapacity())){
            throw new IllegalArgumentException("Room capacity must be greater than 0");
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
