package com.globant.application.port.in;

import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Fiat;

import java.math.BigDecimal;
import java.util.Map;

public interface ViewWalletBalanceUC {
    BigDecimal getWalletBalance();
    Map<Fiat, BigDecimal> getWalletFiats();
    Map<Crypto, BigDecimal> getWalletCryptocurrencies();
    void setWallet(WalletRepository wallet);
}
