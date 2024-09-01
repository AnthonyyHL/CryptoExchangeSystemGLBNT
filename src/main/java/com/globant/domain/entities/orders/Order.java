package com.globant.domain.entities.orders;

import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.ActiveUser;

import java.math.BigDecimal;

public abstract class Order {
    private final User orderEmitter;
    private final Crypto cryptoType;
    private BigDecimal amount;

    public Order(Currency cryptoType, BigDecimal amount) {
        this.cryptoType = (Crypto) cryptoType;
        this.amount = amount;
        orderEmitter = ActiveUser.getInstance().getActiveUser();
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

    public User getOrderEmitter() { return orderEmitter; }
}
