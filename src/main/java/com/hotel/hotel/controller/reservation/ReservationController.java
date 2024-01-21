package com.hotel.hotel.controller.reservation;

import com.hotel.hotel.domain.reservation.Reservation;
import com.hotel.hotel.domain.reservation.ReservationStatus;
import com.hotel.hotel.domain.room.RoomType;
import com.hotel.hotel.domain.reservation.ReservationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(maxAge = 3600, origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDto reservationDto) {
        Reservation newReservation = reservationService.createReservation(
                reservationDto.getClientEmail(),
                reservationDto.getRoomType(),

                LocalDate.parse(reservationDto.getCheckInDate(), DateTimeFormatter.ISO_DATE_TIME),
                reservationDto.getNumberOfNights()
        );
        if (newReservation != null) {
            return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable String reservationId) {
        return reservationService.getReservationById(reservationId)
                .map(reservation -> new ResponseEntity<>(reservation, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{reservationId}/confirm")
    public ResponseEntity<Boolean> confirmReservation(@PathVariable String reservationId) {

        boolean reservationDone = reservationService.confirmReservation(reservationId, ReservationStatus.CONFIRMED);
        return new ResponseEntity<>(reservationDone, reservationDone ?HttpStatus.OK:HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{reservationId}/cancel")
    public ResponseEntity<Boolean> cancelReservation(@PathVariable String reservationId) {
        boolean reservation_cancelled = reservationService.cancelReservation(reservationId);
        return new ResponseEntity<>(reservation_cancelled, reservation_cancelled ?HttpStatus.OK:HttpStatus.NOT_FOUND);
    }

    // Other methods as needed...

    // DTO class for receiving reservation data in requests
    @Getter
    public static class ReservationDto {
        private String clientEmail;
        private RoomType roomType;
        private String checkInDate;
        private int numberOfNights;

        // Getters and setters...
    }
}
