package com.hotel.hotel.controller;

import com.hotel.hotel.errors.UserNotFoundError;
import com.hotel.hotel.errors.WalletNotFoundError;
import com.hotel.hotel.model.client.Client;
import com.hotel.hotel.model.wallet.Currency;
import com.hotel.hotel.services.ClientService;

import java.math.BigDecimal;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hotel.hotel.services.WalletService.convertFromEuro;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "http://localhost:8081") // Replace with your allowed origin(s)
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody ClientDto clientDto) {
        Client newClient = clientService.createClient(clientDto.getFullName(), clientDto.getEmail(), clientDto.getPhoneNumber());
        if (newClient != null) {
            return new ResponseEntity<>(newClient, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{email}/credit/{amount}/{currency}")
    public ResponseEntity<Boolean> creditClientByEmail(@PathVariable String email, @PathVariable int amount, @PathVariable String currency) {
        try {
            clientService.creditClientByEmail(email, BigDecimal.valueOf(amount), Currency.valueOf(currency));
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
        }catch (WalletNotFoundError | UserNotFoundError e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{email}/refund")
    public ResponseEntity<Boolean> refundClientByEmail(@PathVariable String email) {
        try {
            clientService.refundClientByEmail(email);
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        }catch (UserNotFoundError e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{email}/balance/{currency}")
    public ResponseEntity<BigDecimal> creditClientByEmail(@PathVariable String email, @PathVariable String currency) {
        return clientService.getClientById(email)
                .map(client -> new ResponseEntity<>(convertFromEuro(client.getWallet().getBalance(), Currency.valueOf(currency)), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> updateClient(@PathVariable String email, @RequestBody ClientDto clientDto) {
        clientService.updateClient(clientDto.getFullName(), clientDto.getEmail(), clientDto.getPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteClient(@PathVariable String email) {
        clientService.deleteClient(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Other methods as needed...

    // DTO class for receiving client data in requests

    @Getter
    public static class ClientDto {
        private String fullName;
        private String email;
        private String phoneNumber;

        // Getters and setters...
    }
}
