package com.hotel.hotel.repository;

import com.hotel.hotel.model.room.Room;
import com.hotel.hotel.model.room.RoomType;
import com.hotel.hotel.model.wallet.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface WalletRepository extends MongoRepository<Wallet, String> {
    @Query("{ 'type' : ?0 }")
    Optional<Room> findRoomByType(@Param("type") RoomType type);

    @Query("{ 'type' : ?0 }")
    void deleteRoomByType(@Param("type") RoomType type);
    // Custom queries can be added here if needed
}