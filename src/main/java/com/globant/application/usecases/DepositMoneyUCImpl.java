package com.globant.application.usecases;

import com.globant.application.port.in.DepositMoneyUC;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.repositories.Wallet;

import java.math.BigDecimal;

public class DepositMoneyUCImpl implements DepositMoneyUC {
    private Wallet wallet;

    @Override
    public void depositFiat(BigDecimal amount) {
        wallet = ActiveUser.getInstance().getActiveUser().getWallet();

        Fiat referenceFiat = (Fiat) Currency.getReferenceCurrency();
        wallet.deposit(referenceFiat, amount);
    }
}
