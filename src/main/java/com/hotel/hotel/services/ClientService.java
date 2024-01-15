package com.hotel.hotel.services;

import com.hotel.hotel.model.client.Client;
import com.hotel.hotel.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Create a new client
    public Client createClient(String fullName, String email, String phoneNumber) {
        Client newClient = new Client(fullName, email, phoneNumber);
        return clientRepository.save(newClient);
    }

    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Get client by ID
    public Optional<Client> getClientById(String clientId) {
        return clientRepository.findById(clientId);
    }

    // Update client information
    public void updateClient(String clientId, String fullName, String email, String phoneNumber) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        optionalClient.ifPresent(client -> {
            client.setFullName(fullName);
            client.setEmail(email);
            client.setPhoneNumber(phoneNumber);
            clientRepository.save(client);
        });
    }

    // Delete a client
    public void deleteClient(String clientId) {
        clientRepository.deleteById(clientId);
    }

    // Generate a client document (optional, for illustration)
    public void generateClientDocument(String clientId, String filePath) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        optionalClient.ifPresent(client -> {
            // Generate document as needed
            // For simplicity, this example does not include the document generation logic
        });
    }
}


