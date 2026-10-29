package com.example.UniversityMan.service;

import org.springframework.stereotype.Service;
import com.example.UniversityMan.repository.RoomRepository;

@Service
public class RoomService {
    private final RoomRepository roomRepo;

    RoomService(RoomRepository roomRepo){
        this.roomRepo = roomRepo;
    }

    public boolean validateCapacity(double capacity){
        return capacity > 0;
    }

}
