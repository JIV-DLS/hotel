package com.hotel.hotel.controller.room;

import com.hotel.hotel.domain.room.Room;
import com.hotel.hotel.domain.room.RoomType;
import com.hotel.hotel.domain.room.RoomService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody RoomDto roomDto) {
        Room newRoom = roomService.createRoom(roomDto.getType(), roomDto.getPricePerNight(), roomDto.getFeatures());
        if (newRoom != null) {
            return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomType}")
    public ResponseEntity<Room> getRoomByType(@PathVariable RoomType roomType) {
        return roomService.getRoomByType(roomType)
                .map(room -> new ResponseEntity<>(room, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{roomType}")
    public ResponseEntity<Void> updateRoom(@PathVariable RoomType roomType, @RequestBody RoomDto roomDto) {
        roomService.updateRoom(roomType, roomDto.getPricePerNight(), roomDto.getFeatures());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{roomType}")
    public ResponseEntity<Void> deleteRoom(@PathVariable RoomType roomType) {
        roomService.deleteRoom(roomType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Other methods as needed...

    // DTO class for receiving room data in requests
    @Getter
    public static class RoomDto {
        private RoomType type;
        private BigDecimal pricePerNight;
        private Set<String> features;

        // Getters and setters...
    }
}