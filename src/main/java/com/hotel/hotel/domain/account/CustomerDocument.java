package com.hotel.hotel.domain.account;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
@Document(collection = "customer")
public class CustomerDocument {

    @Id
    private String customerId;
    int age;
    @Email(message = "L'adresse e-mail doit être au format valide")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Le numéro de téléphone doit être composé de 10 chiffres")
    private String phoneNumber;


}

