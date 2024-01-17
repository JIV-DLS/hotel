package com.hotel.hotel.model.room;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@Document(collection = "rooms")
public class Room {

    @Id
    private String id;

    private RoomType type;
    private BigDecimal pricePerNight;
    private Set<String> features;

    public Room(RoomType type, BigDecimal pricePerNight, Set<String> features) {
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.features = features;
    }
}

