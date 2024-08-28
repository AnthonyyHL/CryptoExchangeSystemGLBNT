package com.globant.domain.entities.currencies;

import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public class Fiat extends Currency {
    static int fiatIdNumber = 1;
    private static String fiatId;

    public Fiat(String name, BigDecimal price, String shorthandSymbol) {
        super(name, price, shorthandSymbol);
    }
    public static Fiat createInstance(String name, BigDecimal price, String shorthandSymbol) {
        if (!instances.containsKey(shorthandSymbol)) {
            fiatId = MakeId.makeIdNumber(fiatIdNumber);
            instances.put(shorthandSymbol, new Fiat(name, price, shorthandSymbol));
            fiatIdNumber++;
        }
        return (Fiat) instances.get(shorthandSymbol);
    }
    public static Fiat createInstance(String name, BigDecimal price) {
        if (name.length() > 3)
            return createInstance(name, price, name.substring(0, 3).toUpperCase());
        else
            return createInstance(name, price, name.toUpperCase());
    }

    public String getFiatId() {
        return fiatId;
    }

    @Override
    public String toString() {
        return "Fiat{" +
                "cryptoId='" + fiatId + '\'' +
                ", name='" + name + '\'' +
                ", shorthandSymbol='" + shorthandSymbol + '\'' +
                ", price=" + price +
                '}';
    }
}