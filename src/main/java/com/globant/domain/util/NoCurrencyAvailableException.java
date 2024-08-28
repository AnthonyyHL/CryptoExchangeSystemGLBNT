package com.globant.domain.util;

public class NoCurrencyAvailableException extends RuntimeException{
    public NoCurrencyAvailableException(String message) {
        super(message);
    }
}
