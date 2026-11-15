package com.example.universitymanagement.service;

import org.springframework.stereotype.Service;

import com.example.universitymanagement.model.Room;
import com.example.universitymanagement.model.University;
import com.example.universitymanagement.repository.Repo;

import jakarta.annotation.PostConstruct;

import java.util.List;

@Service
public class RoomService {
    private final Repo<Room> roomRepo;
    private final Repo<University> universityRepo;

    RoomService(Repo<Room> roomRepo, Repo<University> universityRepo){
        this.roomRepo = roomRepo;
        this.universityRepo = universityRepo;
    }

    public boolean validateCapacity(double capacity){
        return capacity > 0;
    }

    public boolean validateUniversityId(String universityId){
        return universityRepo.findById(universityId) != null;
    }

    public Room save(Room room){
        if(!validateCapacity(room.getCapacity())){
            throw new IllegalArgumentException("Room capacity must be greater than 0");
        }

        if(!validateUniversityId(room.getUniversityId())){
            throw new IllegalArgumentException("University with id " + room.getUniversityId() + " not found");
        }

        // a re-save with a new object must not lose the existing courses
        Room existing = roomRepo.findById(room.getId());
        if(existing != null){
            room.setCourses(existing.getCourses());
        }

        roomRepo.save(room);

        // unlink first, so a re-save neither duplicates the entry nor leaves it on the old university
        unlink(room.getId());
        universityRepo.findById(room.getUniversityId()).getRooms().add(room);

        return room;
    }

    private void unlink(String id){
        for(University university : universityRepo.findAll()){
            university.getRooms().removeIf(r -> r.getId().equals(id));
        }
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
        Room room = getById(id);

        // courses are real data, so they are not deleted with it and must not point to a missing room
        if(!room.getCourses().isEmpty()){
            throw new IllegalArgumentException("Room with id " + id + " still has courses");
        }

        roomRepo.delete(id);
        unlink(id);
    }

    @PostConstruct
    void linkLoadedRooms(){
        for(Room room : roomRepo.findAll()){
            universityRepo.findById(room.getUniversityId()).getRooms().add(room);
        }
    }
}
