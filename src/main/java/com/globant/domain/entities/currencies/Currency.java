package com.globant.domain.entities.crypto;

import com.globant.domain.util.MakeId;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Currency {
    private static Currency referenceCurrency;

    protected String shorthandSymbol;
    protected String name;
    protected BigDecimal price;

    public Currency(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        if (name.length() > 3)
            this.shorthandSymbol = name.substring(0, 3).toUpperCase();
        else
            this.shorthandSymbol = name;
    }

    public Currency(String name, BigDecimal price, String shorthandSymbol) {
        this(name, price);
        this.shorthandSymbol = shorthandSymbol.toUpperCase();
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

    public BigDecimal getExchangeCurrencyRate(Currency currency) {
        if (this == referenceCurrency)
            return currency.getPrice();
        else if (currency == referenceCurrency)
            return BigDecimal.ONE.divide(this.getPrice(), 8, RoundingMode.HALF_UP);
        else
            return this.getPrice().divide(currency.getPrice(), 8, RoundingMode.HALF_UP);
    }

}
