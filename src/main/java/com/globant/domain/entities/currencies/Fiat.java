package com.globant.domain.entities.crypto;

import com.globant.domain.entities.crypto.Currency;
import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public class Crypto extends Currency {
    static int cryptoIdNumber = 1;
    private final String cryptoId;

    public Crypto(String name, BigDecimal price) {
        super(name, price);
        cryptoId = MakeId.makeIdNumber(cryptoIdNumber);
    }

    public String getCryptoId() {
        return cryptoId;
    }

    @Override
    public String toString() {
        return "Crypto{" +
                "cryptoId='" + cryptoId + '\'' +
                ", shorthandSymbol='" + shorthandSymbol + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}