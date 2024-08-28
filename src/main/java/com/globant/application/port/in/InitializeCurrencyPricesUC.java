package com.globant.application.port.in;

import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Currency;

import java.math.BigDecimal;
import java.util.Map;

public interface InitializeCurrencyPricesUC {
    void loadCurrencies();
    void loadCurrencyOnExchange();
    Map<Currency, BigDecimal> getCurrencyAvailables();
}