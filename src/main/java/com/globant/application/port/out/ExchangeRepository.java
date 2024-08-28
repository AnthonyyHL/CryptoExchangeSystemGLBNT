package com.globant.application.port.out;

import com.globant.domain.entities.currencies.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ExchangeRepository {
    public void addCurrency(Currency currency, BigDecimal amount);
    public void removeCurrency(Currency currency);
    public Map<Currency, BigDecimal> getCurrencies();
    public int buyCurrency(Currency currency, BigDecimal amount);
}
