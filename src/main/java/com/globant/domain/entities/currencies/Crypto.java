package com.globant.domain.entities.currencies;

import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public class Crypto extends Currency {
    static int cryptoIdNumber = 1;
    private static String cryptoId;

    private Crypto(String name, BigDecimal price, String shorthandSymbol) {
        super(name, price, shorthandSymbol);
    }
    public static Crypto createInstance(String name, BigDecimal price, String shorthandSymbol) {
        if (!instances.containsKey(shorthandSymbol)) {
            cryptoId = MakeId.makeIdNumber(cryptoIdNumber);
            instances.put(shorthandSymbol, new Crypto(name, price, shorthandSymbol));
            cryptoIdNumber++;
        }
        return (Crypto) instances.get(shorthandSymbol);
    }
    public static Crypto createInstance(String name, BigDecimal price) {
        if (name.length() > 3)
            return createInstance(name, price, name.substring(0, 3).toUpperCase());
        else
            return createInstance(name, price, name.toUpperCase());
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