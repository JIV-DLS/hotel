package com.hotel.hotel.domain.client;

public class UserNotFoundError extends Exception {
    public UserNotFoundError() {
        super("Sorry the wallet of this user cannot be found");
    }
}