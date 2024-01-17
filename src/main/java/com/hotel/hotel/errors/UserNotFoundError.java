package com.hotel.hotel.errors;

public class UserNotFoundError extends Exception {
    public UserNotFoundError() {
        super("Sorry the wallet of this user cannot be found");
    }
}