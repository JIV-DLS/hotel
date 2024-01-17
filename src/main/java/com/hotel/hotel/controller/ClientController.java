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

    @GetMapping("/{clientId}/credit/{amount}/{currency}")
    public ResponseEntity<Boolean> creditClientById(@PathVariable String clientId, @PathVariable int amount, @PathVariable String currency) {
        try {
            clientService.creditClientById(clientId, BigDecimal.valueOf(amount), Currency.valueOf(currency));
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
        }catch (WalletNotFoundError | UserNotFoundError e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{clientId}/balance/{currency}")
    public ResponseEntity<BigDecimal> creditClientById(@PathVariable String clientId, @PathVariable String currency) {
        return clientService.getClientById(clientId)
                .map(client -> new ResponseEntity<>(convertFromEuro(client.getWallet().getBalance(), Currency.valueOf(currency)), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Void> updateClient(@PathVariable String clientId, @RequestBody ClientDto clientDto) {
        clientService.updateClient(clientId, clientDto.getFullName(), clientDto.getEmail(), clientDto.getPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId) {
        clientService.deleteClient(clientId);
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
