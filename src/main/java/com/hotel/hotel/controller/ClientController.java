package com.hotel.hotel.controller;

import com.hotel.hotel.model.client.Client;
import com.hotel.hotel.services.ClientService;

import java.util.List;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
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

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable String clientId) {
        return clientService.getClientById(clientId)
                .map(client -> new ResponseEntity<>(client, HttpStatus.OK))
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
