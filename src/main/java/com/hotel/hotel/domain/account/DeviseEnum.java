package com.hotel.hotel.domain.account;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "customer")
public enum DeviseEnum {
    EURO(1),
    DOLLAR(0.9),
    LIVRE_STERLING(0.8),
    YEN(0.8),
    FRANC_SUISSE(1.01);

    private double coefficient;
}
