package com.globant.domain.entities.orders;

import com.globant.domain.entities.currencies.Currency;

import java.math.BigDecimal;

public class BuyOrder extends Order{
    private BigDecimal maximumPrice;

    public BuyOrder(Currency cryptoType, BigDecimal amount, BigDecimal maximumPrice) {
        super(cryptoType, amount);
        this.maximumPrice = maximumPrice;
    }

    public BigDecimal getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(BigDecimal maximumPrice) {
        this.maximumPrice = maximumPrice;
    }

    @Override
    public String toString() {
        return "BuyOrder{" +
                "maximumPrice=" + maximumPrice +
                ", cryptoType=" + this.getCryptoType() +
                ", amount=" + this.getAmount() +
                '}';
    }
}
