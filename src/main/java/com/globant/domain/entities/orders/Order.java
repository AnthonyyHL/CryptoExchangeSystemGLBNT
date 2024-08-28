package com.globant.domain.entities;

import com.globant.domain.util.MakeId;

import java.math.BigDecimal;

public abstract class Order {

    static int orderNumberId = 1;
    private final String orderId;
    private User orderEmitter;
    private Crypto cryptoType;
    private BigDecimal amount;

    public Order(Crypto cryptoType, BigDecimal amount) {
        this.cryptoType = cryptoType;
        this.amount = amount;

        orderId = MakeId.makeIdNumber(orderNumberId);
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

    public String getOrderId() {
        return orderId;
    }
}
