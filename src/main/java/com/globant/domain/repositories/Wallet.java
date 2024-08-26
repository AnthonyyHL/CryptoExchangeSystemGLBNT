package com.globant.domain.repositories;

import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.Crypto;
import com.globant.domain.entities.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class Wallet implements WalletRepository {


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
