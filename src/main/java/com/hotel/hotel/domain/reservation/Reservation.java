package com.hotel.hotel.domain.reservation;

import com.hotel.hotel.domain.client.Client;
import com.hotel.hotel.domain.room.Room;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
@Setter
@Getter
@Data
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;

    @DBRef
    private Client client;

    @DBRef
    private Room room;

    private LocalDate checkInDate;
    private int numberOfNights;
    private ReservationStatus status;
    private BigDecimal totalAmount;
    private LocalDate confirmationDate;

    public Reservation(Client client, Room room, LocalDate checkInDate, int numberOfNights, ReservationStatus status, BigDecimal totalAmount, LocalDate confirmationDate) {
        this.client = client;
        this.room = room;
        this.checkInDate = checkInDate;
        this.numberOfNights = numberOfNights;
        this.status = status;
        this.totalAmount = totalAmount;
        this.confirmationDate = confirmationDate;
    }

// Getters and setters...
}
