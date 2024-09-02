package com.globant.domain.entities;

import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.util.MakeId;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    static int transactionNumberId = 1;
    private final String transactionId;
    private Crypto cryptoTraded;
    private BigDecimal amountTraded;
    private BigDecimal priceAtTheMoment;
    private TradeType tradeType;
    private String source;
    private LocalDate transactionDate;
    private LocalTime transactionTime;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Transaction(Crypto cryptoTraded, BigDecimal amountTraded, BigDecimal priceAtTheMoment, TradeType tradeType, String source) {
        this.cryptoTraded = cryptoTraded;
        this.amountTraded = amountTraded;
        this.priceAtTheMoment = priceAtTheMoment;
        this.tradeType = tradeType;
        this.source = source;
        this.transactionDate = LocalDate.now();
        this.transactionTime = LocalTime.now();
        transactionId = MakeId.makeIdNumber(transactionNumberId);
        transactionNumberId++;
    }
    public Transaction(Crypto cryptoTraded, BigDecimal amountTraded, TradeType tradeType, String source){
        this(cryptoTraded, amountTraded, cryptoTraded.getPrice(), tradeType, source);
    }

    public String getTransactionId() { return transactionId; }
    public LocalDate getTransactionDate() { return transactionDate; }

    @Override
    public String toString(){
        return "Cryptocurrency: " + cryptoTraded.getName() + "\n" +
                "Amount: " + amountTraded + "\n" +
                "Price at the moment: " + priceAtTheMoment + "\n" +
                "Trade type: " + tradeType + "\n" +
                "Source: " + source + "\n" +
                "Date: " + transactionDate.format(DATE_FORMATTER) + "\n" +
                "Time: " + transactionTime.format(TIME_FORMATTER) + "\n";
    }

}
