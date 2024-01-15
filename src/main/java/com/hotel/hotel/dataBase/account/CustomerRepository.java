package com.hotel.hotel.dataBase.account;
import com.hotel.hotel.domain.account.CustomerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<CustomerDocument, String> {}
