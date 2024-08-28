package com.globant.domain.entities.orders;

import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.util.InvalidOrderException;

import java.math.BigDecimal;

public class SellOrder extends Order{
    private BigDecimal minimumPrice;
    public SellOrder(Crypto cryptoType, BigDecimal amount, BigDecimal minimumPrice) throws InvalidOrderException {
        super(cryptoType, amount);
        if (cryptoType.getPrice().compareTo(minimumPrice.subtract(new BigDecimal(1))) < 0)
            throw new InvalidOrderException("The minimum price is higher than the current price");
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
