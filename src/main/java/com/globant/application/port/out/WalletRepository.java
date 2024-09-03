package com.globant.application.port.out;

import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.Map;

public interface WalletRepository {
    BigDecimal getBalance();
    Map<Fiat, BigDecimal> getFiats();
    Map<Crypto, BigDecimal> getCryptocurrencies();
    Transaction makeTransaction(Currency currency, BigDecimal amount, TradeType tradeType, String source);
    Transaction makeOrderTransaction(Currency currency, BigDecimal amount, BigDecimal priceAtTheMoment, TradeType tradeType, String source);
    void updateBalance(Fiat fiat, BigDecimal amount);
    void addCryptocurrency(Currency currency, BigDecimal amount);
    void deposit(Currency currency, BigDecimal amount);
}