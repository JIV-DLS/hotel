package com.hotel.hotel.repository;

import com.hotel.hotel.model.client.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
    // Custom queries can be added here if needed
}

