package com.hotel.hotel.services;

import com.hotel.hotel.model.room.Room;
import com.hotel.hotel.model.room.RoomType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.hotel.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Create a new room
    public Room createRoom(RoomType type, BigDecimal pricePerNight, Set<String> features) {
        Room newRoom = new Room(type, pricePerNight, features);
        return roomRepository.save(newRoom);
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by type
    public Optional<Room> getRoomByType(RoomType type) {
        return roomRepository.findRoomByType(type);
    }

    // Update room information
    public void updateRoom(RoomType type, BigDecimal pricePerNight, Set<String> features) {
        Optional<Room> optionalRoom = roomRepository.findRoomByType(type);
        optionalRoom.ifPresent(room -> {
            room.setPricePerNight(pricePerNight);
            room.setFeatures(features);
            roomRepository.save(room);
        });
    }

    // Delete a room
    public void deleteRoom(RoomType type) {
        roomRepository.deleteRoomByType(type);
    }
}


