package com.hotel.hotel.errors;

public class WalletNotFoundError extends Exception {
    public WalletNotFoundError() {
        super("Sorry the wallet of this user cannot be found");
    }
}