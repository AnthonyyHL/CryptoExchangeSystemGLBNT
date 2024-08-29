package com.globant.domain.entities.currencies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public abstract class Currency {
    protected static Map<String, Currency> instances = new HashMap<>();
    private static Currency referenceCurrency;

    protected String shorthandSymbol;
    protected String name;
    protected BigDecimal price;

    protected Currency(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        if (name.length() > 3)
            this.shorthandSymbol = name.substring(0, 3).toUpperCase();
        else
            this.shorthandSymbol = name;
    }

    protected Currency(String name, BigDecimal price, String shorthandSymbol) {
        this(name, price);
        this.shorthandSymbol = shorthandSymbol.toUpperCase();
    }
    public static Currency getInstance(String shorthandSymbol) {
        return instances.get(shorthandSymbol);
    }

    public String getShorthandSymbol() {
        return shorthandSymbol;
    }
    public String getName() {
        return name;
    }
    public BigDecimal getPrice() { return price; }
    public void updatePrice(BigDecimal newPrice) { this.price = newPrice; }

    public static void setReferenceCurrency(Currency currency) {
        referenceCurrency = currency;
    }

    public BigDecimal getExchangeCurrencyRate() {
        if (this == referenceCurrency)
            return price;
        else
            return price.divide(referenceCurrency.price, 8, RoundingMode.HALF_UP);
    }

}
