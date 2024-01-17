package com.hotel.hotel.domain.wallet;

public class WalletNotFoundError extends Exception {
    public WalletNotFoundError() {
        super("Sorry the wallet of this user cannot be found");
    }
}