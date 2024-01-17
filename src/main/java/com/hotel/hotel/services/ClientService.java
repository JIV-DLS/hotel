package com.hotel.hotel.services;

import com.hotel.hotel.errors.UserNotFoundError;
import com.hotel.hotel.errors.WalletNotFoundError;
import com.hotel.hotel.model.client.Client;
import com.hotel.hotel.model.wallet.Currency;
import com.hotel.hotel.model.wallet.Wallet;
import com.hotel.hotel.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final WalletService walletService;

    @Autowired
    public ClientService(ClientRepository clientRepository, WalletService walletService) {
        this.clientRepository = clientRepository;
        this.walletService = walletService;
    }

    // Create a new client
    public Client createClient(String fullName, String email, String phoneNumber) {
        Client newClient = new Client(fullName, email, phoneNumber, walletService.createWallet());
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
    public boolean creditClientById(String clientId, BigDecimal amount, Currency currency) throws WalletNotFoundError, UserNotFoundError {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()){
            throw new UserNotFoundError();
        }
        Client client = optionalClient.get();

        walletService.credit(client.getWallet(), amount, currency);

        return true;
    }
    public boolean debitClientById(String clientId, BigDecimal amount, Currency currency) throws WalletNotFoundError, UserNotFoundError {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty()){
            throw new UserNotFoundError();
        }
        Client client = optionalClient.get();

        walletService.debit(client.getWallet(), amount, currency);

        return true;
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


