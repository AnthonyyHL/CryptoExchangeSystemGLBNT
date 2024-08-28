package com.globant.domain.entities;

import java.math.BigDecimal;

public class BuyOrder extends Order{
    private BigDecimal maximumPrice;

    public BuyOrder(Crypto cryptoType, BigDecimal amount, BigDecimal maximumPrice) {
        super(cryptoType, amount);
        this.maximumPrice = maximumPrice;
    }

    public BigDecimal getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(BigDecimal maximumPrice) {
        this.maximumPrice = maximumPrice;
    }

    public String toString() {
        return "BuyOrder{" +
                "maximumPrice=" + maximumPrice +
                ", cryptoType=" + this.getCryptoType() +
                ", amount=" + this.getAmount() +
                '}';
    }
}
