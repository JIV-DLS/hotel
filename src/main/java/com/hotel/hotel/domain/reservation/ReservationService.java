package com.hotel.hotel.domain.reservation;

import com.hotel.hotel.domain.client.ClientService;
import com.hotel.hotel.domain.client.UserNotFoundError;
import com.hotel.hotel.domain.wallet.WalletNotFoundError;
import com.hotel.hotel.domain.client.Client;
import com.hotel.hotel.domain.room.Room;
import com.hotel.hotel.domain.room.RoomType;
import com.hotel.hotel.dataBase.client.ClientRepository;
import com.hotel.hotel.dataBase.reservation.ReservationRepository;
import com.hotel.hotel.dataBase.room.RoomRepository;
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
                this.clientService.debitClientByEmail(clientId, totalAmount.divide(BigDecimal.valueOf(2), RoundingMode.CEILING));
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
    public List<Reservation> getReservationsByClientId(String email) {
        return reservationRepository.findReservationsByClientId(email);
    }
    // Update reservation status
    public boolean confirmReservation(String reservationId, ReservationStatus newStatus) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            try {
                this.clientService.debitClientByEmail(reservation.getClient().getEmail(), reservation.getTotalAmount().divide(BigDecimal.valueOf(2), RoundingMode.CEILING));
            } catch(Exception e) {
                return false;
            }

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
    public void confirmReservation(Reservation reservation, ReservationStatus newStatus) {
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

            reservation.setStatus(ReservationStatus.CANCELED);
            // You may want to implement additional logic related to cancellation,
            // such as handling refunds or sending notifications to clients.

            reservationRepository.save(reservation);
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

