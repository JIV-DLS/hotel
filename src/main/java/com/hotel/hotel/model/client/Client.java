package com.hotel.hotel.model.client;

import com.hotel.hotel.model.wallet.Wallet;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Document(collection = "clients")
public class Client {
    private String fullName;
    @Email(message = "L'adresse e-mail doit être au format valide")
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "Le numéro de téléphone doit être composé de 10 chiffres")
    private String phoneNumber;
    @DBRef
    private Wallet wallet;
    @Id
    private String clientId; // Generated randomly

    public Client(String fullName, String email, String phoneNumber, Wallet wallet) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.wallet = wallet;
        this.clientId = generateRandomClientId();
    }

    // Getters and setters

    // Method to generate a random client ID
    private String generateRandomClientId() {
        return UUID.randomUUID().toString();
    }
}

