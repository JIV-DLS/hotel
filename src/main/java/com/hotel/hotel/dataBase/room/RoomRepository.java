package com.hotel.hotel.dataBase.room;

import com.hotel.hotel.domain.room.Room;
import com.hotel.hotel.domain.room.RoomType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RoomRepository extends MongoRepository<Room, String> {
    @Query("{ 'type' : ?0 }")
    Optional<Room> findRoomByType(@Param("type") RoomType type);

    @Query("{ 'type' : ?0 }")
    void deleteRoomByType(@Param("type") RoomType type);
    // Custom queries can be added here if needed
}