package com.hotel.hotel.services;

import com.hotel.hotel.errors.ErrorResponse;
import com.hotel.hotel.model.wallet.Currency;
import com.hotel.hotel.model.wallet.Wallet;
import com.hotel.hotel.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.hotel.hotel.model.wallet.Currency.EURO;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    // Create a new client
    public Wallet createWallet() {
        Wallet newWallet = new Wallet(BigDecimal.valueOf(0),EURO);
        return walletRepository.save(newWallet);
    }

    // Get client by ID
    public Optional<Wallet> getWalletById(String walletId) {
        return walletRepository.findById(walletId);
    }

    private static final Map<Currency, BigDecimal> EXCHANGE_RATES_TO_EURO;
    private static final Map<Currency, BigDecimal> EXCHANGE_RATES_FROM_EURO;


    static {
        EXCHANGE_RATES_TO_EURO = new HashMap<>();
        EXCHANGE_RATES_TO_EURO.put(Currency.EURO, BigDecimal.ONE);
        EXCHANGE_RATES_TO_EURO.put(Currency.DOLLAR, new BigDecimal("1.12")); // Example rate: 1 USD = 0.89 EUR
        EXCHANGE_RATES_TO_EURO.put(Currency.POUND_STERLING, new BigDecimal("0.85")); // Example rate: 1 GBP = 1.18 EUR
        EXCHANGE_RATES_TO_EURO.put(Currency.YEN, new BigDecimal("130.71")); // Example rate: 1 JPY = 0.0077 EUR
        EXCHANGE_RATES_TO_EURO.put(Currency.SWISS_FRANC, new BigDecimal("1.06")); // Example rate: 1 CHF = 0.94 EUR

        EXCHANGE_RATES_FROM_EURO = new HashMap<>();
        EXCHANGE_RATES_FROM_EURO.put(Currency.EURO, BigDecimal.ONE);
        EXCHANGE_RATES_FROM_EURO.put(Currency.DOLLAR, new BigDecimal("1.12")); // Example rate: 1 USD = 0.89 EUR
        EXCHANGE_RATES_FROM_EURO.put(Currency.POUND_STERLING, new BigDecimal("0.85")); // Example rate: 1 GBP = 1.18 EUR
        EXCHANGE_RATES_FROM_EURO.put(Currency.YEN, new BigDecimal("130.71")); // Example rate: 1 JPY = 0.0077 EUR
        EXCHANGE_RATES_FROM_EURO.put(Currency.SWISS_FRANC, new BigDecimal("1.06")); // Example rate: 1 CHF = 0.94 EUR
    }

    public Wallet credit(Wallet wallet, BigDecimal amount, Currency currency) {
        //validateCurrency(wallet, currency);
        BigDecimal amountInEuro = convertToEuro(amount, currency);
        wallet.setBalance(wallet.getBalance().add(amountInEuro));

        return walletRepository.save(wallet);
    }

    public void debit(Wallet wallet, BigDecimal amountInEuro) {
        //validateCurrency(wallet, currency);
        if (wallet.getBalance().compareTo(amountInEuro) < 0) {
            throw new IllegalArgumentException("Insufficient funds in the wallet");
        }
        wallet.setBalance(wallet.getBalance().subtract(amountInEuro));
        walletRepository.save(wallet);
    }

    public void debit(Wallet wallet, BigDecimal amount, Currency currency) {
        BigDecimal amountInEuro = convertToEuro(amount, currency);
        debit(wallet, amountInEuro);
    }

    private void validateCurrency(Wallet wallet, Currency currency) {
        if (currency != Currency.EURO && wallet.getCurrency() != currency) {
            throw new IllegalArgumentException("Currency mismatch. Wallet currency is " + wallet.getCurrency());
        }
    }

    private BigDecimal convertToEuro(BigDecimal amount, Currency currency) {
        if (currency == Currency.EURO) {
            return amount;
        }
        BigDecimal exchangeRate = EXCHANGE_RATES_TO_EURO.get(currency);
        if (exchangeRate == null) {
            throw new IllegalArgumentException("Exchange rate not available for currency: " + currency);
        }
        return amount.divide(exchangeRate, RoundingMode.CEILING);
    }
    public static BigDecimal convertFromEuro(BigDecimal amount, Currency targetCurrency) {
        //if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        //    throw new IllegalArgumentException("Amount must be greater than zero");
        //}

        BigDecimal exchangeRate = EXCHANGE_RATES_FROM_EURO.get(targetCurrency);
        if (exchangeRate == null) {
            throw new IllegalArgumentException("Invalid target currency");
        }

        return amount.multiply(exchangeRate);
    }

    public void refund(Wallet wallet) {
        wallet.setBalance(BigDecimal.valueOf(0));
        walletRepository.save(wallet);
    }
}


