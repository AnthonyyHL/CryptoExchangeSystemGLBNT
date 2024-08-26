package com.globant.application.port.out;

import com.globant.domain.entities.Crypto;
import com.globant.domain.entities.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface WalletRepo {
    BigDecimal getBalance();
    List<Crypto> getCryptocurrencies();
    List<Transaction> getTransactions();
    void updateBalance();
    void addCryptocurrency();
}