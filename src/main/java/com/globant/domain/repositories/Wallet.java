package com.globant.domain.repositories;

import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.currencies.Fiat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Wallet implements WalletRepository {
    Map<Crypto, BigDecimal> cryptocurrencies;
    Map<Fiat, BigDecimal> fiats;
    BigDecimal balance;

    @Override
    public BigDecimal getBalance() {
        return null;
    }

    @Override
    public List<Crypto> getCryptocurrencies() {
        return null;
    }

    @Override
    public List<Transaction> getTransactions() {
        return null;
    }

    @Override
    public void updateBalance() {

    }

    @Override
    public void addCryptocurrency() {

    }
}
