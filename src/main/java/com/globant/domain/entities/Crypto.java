package com.globant.domain.entities;

import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public class Crypto {
    static int cryptoIdNumber = 1;
    private final String cryptoId;
    private String shorthandSymbol;
    protected String name;
    protected BigDecimal price;

    public Crypto(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        cryptoId = MakeId.makeIdNumber(cryptoIdNumber);
        if (name.length() > 3)
            this.shorthandSymbol = name.substring(0, 3).toUpperCase();
        else
            this.shorthandSymbol = name;
    }
    public Crypto(String name, BigDecimal price, String shorthandSymbol) {
        this(name, price);
        this.shorthandSymbol = shorthandSymbol.toUpperCase();
    }

    public String getShorthandSymbol() {
        return shorthandSymbol;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCryptoId() {
        return cryptoId;
    }
}