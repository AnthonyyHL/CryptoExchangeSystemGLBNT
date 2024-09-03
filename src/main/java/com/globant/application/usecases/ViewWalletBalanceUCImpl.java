package com.globant.application.usecases;

import com.globant.application.port.in.ViewWalletBalanceUC;
import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Fiat;

import java.math.BigDecimal;
import java.util.Map;

public class ViewWalletBalanceUCImpl implements ViewWalletBalanceUC {
    private WalletRepository wallet;
    @Override
    public BigDecimal getWalletBalance() {
        return wallet.getBalance();
    }
    @Override
    public Map<Fiat, BigDecimal> getWalletFiats() {
        return wallet.getFiats();
    }
    @Override
    public Map<Crypto, BigDecimal> getWalletCryptocurrencies() {
        return wallet.getCryptocurrencies();
    }
    @Override
    public void setWallet(WalletRepository wallet) { this.wallet = wallet; }
}
