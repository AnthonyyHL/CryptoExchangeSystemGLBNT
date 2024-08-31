package com.globant.domain.entities;

import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;

public class Transaction {
    private Crypto cryptoTraded;
    private BigDecimal amountTraded;
    private BigDecimal priceAtTheMoment;
    private TradeType tradeType;

    public Transaction(Crypto cryptoTraded, BigDecimal amountTraded, TradeType tradeType){
        this.cryptoTraded = cryptoTraded;
        this.amountTraded = amountTraded;
        this.tradeType = tradeType;
        this.priceAtTheMoment = cryptoTraded.getPrice();
    }

    @Override
    public String toString(){
        return "Cryptocurrency: " + cryptoTraded.getName() + "\n" +
                "Amount: " + amountTraded + "\n" +
                "Price at the moment: " + priceAtTheMoment + "\n" +
                "Trade type: " + tradeType + "\n";
    }

}
