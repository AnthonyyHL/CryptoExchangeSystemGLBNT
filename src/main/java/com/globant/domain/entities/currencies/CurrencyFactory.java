package com.globant.domain.entities.currencies;

import com.globant.domain.util.NoCurrencyAvailableException;

import java.math.BigDecimal;

public class CurrencyFactory {
    public static Currency createCurrency(String type, String symbol, String name, BigDecimal price) {
        switch(type.toLowerCase()) {
            case "fiat":
                return Fiat.createInstance(name, price, symbol);
            case "crypto":
                return Crypto.createInstance(name, price, symbol);
            default:
                throw new NoCurrencyAvailableException("Invalid currency type.");
        }
    }
    public static Currency createCurrency(String type, String name, BigDecimal price) {
        switch(type.toLowerCase()) {
            case "fiat":
                return Fiat.createInstance(name, price);
            case "crypto":
                return Crypto.createInstance(name, price);
            default:
                throw new NoCurrencyAvailableException("Invalid currency type.");
        }
    }
}