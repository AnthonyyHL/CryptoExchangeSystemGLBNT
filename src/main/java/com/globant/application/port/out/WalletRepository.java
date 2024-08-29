package com.globant.application.port.out;

import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.currencies.Fiat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WalletRepository {
    BigDecimal getBalance();
    Map<Fiat, BigDecimal> getFiats();
    Map<Crypto, BigDecimal> getCryptocurrencies();
    Transaction makeTransaction();
    public void updateBalance(Fiat fiat, BigDecimal amount);
    void addCryptocurrency();
}