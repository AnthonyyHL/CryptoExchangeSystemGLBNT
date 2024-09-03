package com.globant.application.port.in;

import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface InitializeCurrencyPricesUC {
    void loadCurrencies();
    void loadCurrencyOnExchange();
    Map<Currency, BigDecimal> getCurrencyAvailables();

    void loadFiatOnSystem();

    List<Fiat> getFiatAvailables();
}