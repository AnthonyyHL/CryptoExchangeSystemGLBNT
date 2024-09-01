package com.globant.domain.entities.orders;

import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public class BuyOrder extends Order{
    static int buyOrderNumberId = 1;
    private final String buyOrderId;
    private BigDecimal maximumPrice;

    public BuyOrder(Currency cryptoType, BigDecimal amount, BigDecimal maximumPrice) {
        super(cryptoType, amount);
        this.maximumPrice = maximumPrice;
        buyOrderId = MakeId.makeIdNumber(buyOrderNumberId);
        buyOrderNumberId++;
    }

    public BigDecimal getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(BigDecimal maximumPrice) {
        this.maximumPrice = maximumPrice;
    }

    public String getBuyOrderId() { return buyOrderId; }

    @Override
    public String toString() {
        return "BuyOrder{" +
                "maximumPrice=" + maximumPrice +
                ", cryptoType=" + this.getCryptoType() +
                ", amount=" + this.getAmount() +
                '}';
    }
}
