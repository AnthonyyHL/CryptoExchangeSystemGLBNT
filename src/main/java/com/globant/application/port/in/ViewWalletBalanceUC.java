package com.globant.application.port.in;

import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.repositories.Wallet;

import java.math.BigDecimal;
import java.util.Map;

public interface ViewWalletBalanceUC {
    public BigDecimal viewWalletBalance();
    public Map<Fiat, BigDecimal> viewWalletFiats();
    public Map<Crypto, BigDecimal> viewWalletCryptocurrencies();
    public void deposit(String fiat, BigDecimal amount);
    public void setWallet(Wallet wallet);
}
