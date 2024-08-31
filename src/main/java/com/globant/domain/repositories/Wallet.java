package com.globant.domain.repositories;

import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet implements WalletRepository {
    Map<Crypto, BigDecimal> cryptocurrencies;
    Map<Fiat, BigDecimal> fiats;
    BigDecimal balance;

    public Wallet() {
        this.cryptocurrencies = new HashMap<>();
        this.fiats = new HashMap<>();
        this.balance = new BigDecimal(0);
    }

    @Override
    public BigDecimal getBalance() { return balance; }

    @Override
    public Map<Fiat, BigDecimal> getFiats() {
        return fiats;
    }

    @Override
    public Map<Crypto, BigDecimal> getCryptocurrencies() {
        return cryptocurrencies;
    }

    @Override
    public Transaction makeTransaction(Currency currency, BigDecimal amount, TradeType tradeType, String source) {
        return new Transaction((Crypto) currency, amount, tradeType, source);
    }

    @Override
    public void updateBalance(Fiat fiat, BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public void addCryptocurrency(Currency currency, BigDecimal amount) {
        Crypto crypto = (Crypto) currency;
        if (cryptocurrencies.isEmpty() || !cryptocurrencies.containsKey(crypto))
            cryptocurrencies.put(crypto, BigDecimal.ZERO);
        cryptocurrencies.put(crypto, cryptocurrencies.get(crypto).add(amount));
    }
    @Override
    public void deposit(Currency currency, BigDecimal amount) {
        Fiat fiat = (Fiat) currency;
        if (fiats.isEmpty() || !fiats.containsKey(fiat))
            fiats.put(fiat, BigDecimal.ZERO);
        fiats.put(fiat, fiats.get(fiat).add(amount));
        updateBalance(fiat, amount);
    }
}
