package com.hotel.hotel.model.account;

import com.hotel.hotel.dataBase.account.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Flux<CustomerDocument> getAll(){

        return customerRepository.findAll();
    }
    public Mono<CustomerDocument> save(CustomerDocument customer)
    {
        return customerRepository.save(customer);
    }

    public Mono<CustomerDocument> getById(String id)
    {
        return customerRepository.findById(id);
    }

    public Mono<Void> deleteById(String id)
    {
        return customerRepository.deleteById(id);
    }
    public Mono<CustomerDocument> update(String id, CustomerDocument customer)
    {
        return customerRepository.findById(id)
                .flatMap(existingCustomer -> {
                    existingCustomer.setAge(customer.getAge());
                    existingCustomer.setEmail(customer.getEmail());
                    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                    return customerRepository.save(existingCustomer);
                });
    }



}
