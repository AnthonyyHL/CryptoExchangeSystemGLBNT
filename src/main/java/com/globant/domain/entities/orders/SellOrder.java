package com.globant.domain.entities.orders;

import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.util.InvalidOrderException;
import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public class SellOrder extends Order{
    static int sellOrderNumberId = 1;
    private final String sellOrderId;
    private BigDecimal minimumPrice;
    public SellOrder(Currency cryptoType, BigDecimal amount, BigDecimal minimumPrice) {
        super(cryptoType, amount);
        this.minimumPrice = minimumPrice;
        sellOrderId = MakeId.makeIdNumber(sellOrderNumberId);
        sellOrderNumberId++;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public String getSellOrderId() { return sellOrderId; }

    public String toString() {
        return "SellOrder{" +
                "minimumPrice=" + minimumPrice +
                ", cryptoType=" + this.getCryptoType() +
                ", amount=" + this.getAmount() +
                '}';
    }

}
