package com.hotel.hotel.domain.room;

import com.hotel.hotel.dataBase.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
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
        List<Room> gottenRooms = roomRepository.findAll();
        if (gottenRooms.isEmpty()){
            createRoom(RoomType.STANDARD, BigDecimal.valueOf(50), new HashSet<>(Set.of("Lit 1 place", "Wifi", "TV")));
            createRoom(RoomType.SUPERIOR, BigDecimal.valueOf(100), new HashSet<>(Set.of("Lit 2 places", "Wifi", "TV écran plat", "Minibar", "Climatiseur")));
            createRoom(RoomType.SUITE, BigDecimal.valueOf(200), new HashSet<>(Set.of("Lit 2 places", "Wifi", "TV écran plat", "Minibar", "Climatiseur", "Baignoire", "Terrasse")));
            return getAllRooms();
        }
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


