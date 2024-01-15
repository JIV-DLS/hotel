package com.hotel.hotel.repository;

import com.hotel.hotel.model.reservation.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    @Query("{ 'client.id' : ?0 }")
    List<Reservation> findReservationsByClientId(@Param("clientId") String clientId);
    // Custom queries can be added here if needed
}
