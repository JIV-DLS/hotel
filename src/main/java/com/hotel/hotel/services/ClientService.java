package com.hotel.hotel.services;

import com.hotel.hotel.errors.UserNotFoundError;
import com.hotel.hotel.errors.WalletNotFoundError;
import com.hotel.hotel.model.client.Client;
import com.hotel.hotel.model.wallet.Currency;
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
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        if (optionalClient.isPresent()){
            return optionalClient.get();
        }
        Client newClient = new Client(fullName, email, phoneNumber, walletService.createWallet());
        return clientRepository.save(newClient);
    }

    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Get client by ID
    public Optional<Client> getClientById(String email) {
        return clientRepository.findByEmail(email);
    }
    public boolean creditClientByEmail(String email, BigDecimal amount, Currency currency) throws WalletNotFoundError, UserNotFoundError {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        if (optionalClient.isEmpty()){
            throw new UserNotFoundError();
        }
        Client client = optionalClient.get();

        walletService.credit(client.getWallet(), amount, currency);

        return true;
    }
    public boolean debitClientByEmail(String email, BigDecimal amount) throws WalletNotFoundError, UserNotFoundError {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        if (optionalClient.isEmpty()){
            throw new UserNotFoundError();
        }
        Client client = optionalClient.get();

        walletService.debit(client.getWallet(), amount);

        return true;
    }

    // Update client information
    public void updateClient(String fullName, String email, String phoneNumber) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        optionalClient.ifPresent(client -> {
            client.setFullName(fullName);
            client.setEmail(email);
            client.setPhoneNumber(phoneNumber);
            clientRepository.save(client);
        });
    }

    // Delete a client
    public void deleteClient(String email) {
        clientRepository.deleteById(email);
    }

    // Generate a client document (optional, for illustration)
    public void generateClientDocument(String email, String filePath) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        optionalClient.ifPresent(client -> {
            // Generate document as needed
            // For simplicity, this example does not include the document generation logic
        });
    }

    public boolean refundClientByEmail(String email) throws UserNotFoundError {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        if (optionalClient.isEmpty()){
            throw new UserNotFoundError();
        }
        Client client = optionalClient.get();

        walletService.refund(client.getWallet());

        return true;
    }
}


