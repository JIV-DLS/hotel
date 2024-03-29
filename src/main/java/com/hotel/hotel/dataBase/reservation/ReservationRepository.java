package com.hotel.hotel.dataBase.reservation;

import com.hotel.hotel.domain.reservation.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    @Query("{ 'client.email' : ?0 }")
    List<Reservation> findReservationsByClientId(@Param("email") String email);
    // Custom queries can be added here if needed
}
