package com.globant.domain.entities.orders;

import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.util.InvalidOrderException;

import java.math.BigDecimal;

public class SellOrder extends Order{
    private BigDecimal minimumPrice;
    public SellOrder(Currency cryptoType, BigDecimal amount, BigDecimal minimumPrice) throws InvalidOrderException {
        super(cryptoType, amount);
        this.minimumPrice = minimumPrice;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public String toString() {
        return "SellOrder{" +
                "minimumPrice=" + minimumPrice +
                ", cryptoType=" + this.getCryptoType() +
                ", amount=" + this.getAmount() +
                '}';
    }

}
