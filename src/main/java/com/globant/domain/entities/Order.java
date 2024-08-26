package com.globant.domain.entities;

import java.math.BigDecimal;

public abstract class Order {
    private Crypto cryptoType;
    private BigDecimal amount;

    public Order(Crypto cryptoType, BigDecimal amount) {
        this.cryptoType = cryptoType;
        this.amount = amount;
    }

    public Crypto getCryptoType() {
        return cryptoType;
    }

    public void setCryptoType(Crypto cryptoType) {
        this.cryptoType = cryptoType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
