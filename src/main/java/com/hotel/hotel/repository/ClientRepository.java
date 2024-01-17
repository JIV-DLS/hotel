package com.hotel.hotel.repository;

import com.hotel.hotel.model.client.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {
    @Query("{ 'email' : ?0 }")
    Optional<Client> findByEmail(String email);
    // Custom queries can be added here if needed
}

