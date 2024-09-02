package com.globant.application.port.in;

import com.globant.domain.entities.currencies.Currency;

import java.math.BigDecimal;

public interface DepositMoneyUC {
    void depositFiat(Currency fiat, BigDecimal amount);
}
