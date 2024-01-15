package com.hotel.hotel.controller.account;


import com.hotel.hotel.domain.account.CustomerDocument;
import com.hotel.hotel.domain.account.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Flux<CustomerDocument> getAll() {

        return customerService.getAll();
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CustomerDocument> createVol(@RequestBody CustomerDocument customer) {
        return customerService.save(customer);
    }

    @GetMapping("/{id}")
    public Mono<CustomerDocument> getById(String id)
    {
        return customerService.getById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(String id)
    {
        return customerService.deleteById(id);
    }
    @PostMapping("/{id}")
    public Mono<CustomerDocument> update(@PathVariable String id, @Valid @RequestBody CustomerDocument customer)
    {
        return customerService.update(id, customer);
    }
}
