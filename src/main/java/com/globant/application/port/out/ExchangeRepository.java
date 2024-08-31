package com.globant.application.port.out;

import com.globant.domain.entities.currencies.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ExchangeRepository {
    void addCurrency(Currency currency, BigDecimal amount);
    void removeCurrency(Currency currency);
    Map<Currency, BigDecimal> getCurrencies();
    int buyCurrency(Currency currency, BigDecimal amount);
}
