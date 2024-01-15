package com.hotel.hotel.domain.account;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    public Mono<Double> convert(DeviseEnum devise, double price){
        return Mono.just(price*devise.getCoefficient());
        
    }
}
