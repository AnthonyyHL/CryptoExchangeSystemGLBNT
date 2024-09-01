package com.globant.domain.entities.orders;

import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public abstract class Order {

    static int orderNumberId = 1;
    private static String orderId;
    private final User orderEmitter;
    private Crypto cryptoType;
    private BigDecimal amount;

    public Order(Currency cryptoType, BigDecimal amount) {
        this.cryptoType = (Crypto) cryptoType;
        this.amount = amount;
        orderEmitter = ActiveUser.getInstance().getActiveUser();

        orderId = MakeId.makeIdNumber(orderNumberId);
    }

    public Crypto getCryptoType() {
        return cryptoType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public User getOrderEmitter() { return orderEmitter; }
}
