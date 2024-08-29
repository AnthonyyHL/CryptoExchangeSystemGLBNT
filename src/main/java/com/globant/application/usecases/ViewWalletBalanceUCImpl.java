package com.globant.application.usecases;

import com.globant.application.port.in.ViewWalletBalanceUC;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.repositories.Wallet;

import java.math.BigDecimal;
import java.util.Map;

public class ViewWalletBalanceUCImpl implements ViewWalletBalanceUC {
    private final Wallet wallet;
    private final Exchange exchange;
    public ViewWalletBalanceUCImpl(Wallet wallet, Exchange exchange) {
        this.wallet = wallet;
        this.exchange = exchange;
    }

    @Override
    public BigDecimal viewWalletBalance() {
        return wallet.getBalance();
    }
    @Override
    public Map<Fiat, BigDecimal> viewWalletFiats() {
        return wallet.getFiats();
    }
    @Override
    public Map<Crypto, BigDecimal> viewWalletCryptocurrencies() {
        return wallet.getCryptocurrencies();
    }
    @Override
    public void deposit(String fiat, BigDecimal amount) {
        exchange.getCurrencies().forEach((currency, quantity) -> {
            if (currency.getName().equals(fiat)) {
                wallet.deposit((Fiat) currency, amount);
            }
        });
    }
}
