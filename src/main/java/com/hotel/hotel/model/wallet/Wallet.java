package com.hotel.hotel.model.wallet;

import java.math.BigDecimal;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor(force = true)
@Data
@Document(collection = "wallets")
public class Wallet {

    @Id
    private String id;

    public Wallet(BigDecimal balance, Currency currency) {
        this.balance = balance;
        this.currency = currency;
    }

    private BigDecimal balance;
    private Currency currency;

}
