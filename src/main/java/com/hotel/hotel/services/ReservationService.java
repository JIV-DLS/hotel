package com.hotel.hotel.services;

import com.hotel.hotel.errors.UserNotFoundError;
import com.hotel.hotel.errors.WalletNotFoundError;
import com.hotel.hotel.model.client.Client;
import com.hotel.hotel.model.reservation.Reservation;
import com.hotel.hotel.model.reservation.ReservationStatus;
import com.hotel.hotel.model.room.Room;
import com.hotel.hotel.model.room.RoomType;
import com.hotel.hotel.model.wallet.Currency;
import com.hotel.hotel.repository.ClientRepository;
import com.hotel.hotel.repository.ReservationRepository;
import com.hotel.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, ClientRepository clientRepository, ClientService clientService) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    // Create a new reservation
    public Reservation createReservation(String clientId, RoomType roomType, LocalDate checkInDate, int numberOfNights) {
        Optional<Room> optionalRoom = roomRepository.findRoomByType(roomType);
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        if (optionalRoom.isPresent() && optionalClient.isPresent()) {
            Room room = optionalRoom.get();
            Client client = optionalClient.get();

            BigDecimal totalAmount = room.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
            try {
                this.clientService.debitClientById(clientId, totalAmount.divide(BigDecimal.valueOf(2), RoundingMode.CEILING), Currency.EURO);
                Reservation newReservation = new Reservation(client, room, checkInDate, numberOfNights, ReservationStatus.REGISTERED, totalAmount, null);

                return reservationRepository.save(newReservation);
            } catch (WalletNotFoundError | UserNotFoundError e) {
                return null;
            }
        } else {
            return null; // Room or client not found
        }
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get reservations for a specific client
    public List<Reservation> getReservationsByClientId(String clientId) {
        return reservationRepository.findReservationsByClientId(clientId);
    }
    // Update reservation status
    public boolean updateReservationStatus(String reservationId, ReservationStatus newStatus) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(newStatus);

            if (newStatus == ReservationStatus.CONFIRMED) {
                reservation.setConfirmationDate(LocalDate.now());
            }

            reservationRepository.save(reservation);
            return true;

        }
        return false;
    }
    // Update reservation status
    public void updateReservationStatus(Reservation reservation, ReservationStatus newStatus) {
        reservation.setStatus(newStatus);
        if (newStatus == ReservationStatus.CONFIRMED) {
            reservation.setConfirmationDate(LocalDate.now());
        }
        reservationRepository.save(reservation);
    }
    // Cancel reservation
    public boolean cancelReservation(String reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            // You may want to implement additional logic related to cancellation,
            // such as handling refunds or sending notifications to clients.

            reservationRepository.delete(reservation);
            return true; // Cancellation successful
        }

        return false; // Reservation not found
    }
    // Cancel a reservation
    public void cancelReservation(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.REGISTERED || reservation.getStatus() == ReservationStatus.CONFIRMED) {
            reservationRepository.delete(reservation);
        }
    }

    public Optional<Reservation> getReservationById(String reservationId) {
        return reservationRepository.findById(reservationId);
    }
}

