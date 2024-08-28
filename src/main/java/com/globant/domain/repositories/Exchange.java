package com.globant.domain.repositories;

import com.globant.application.port.out.ExchangeRepository;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.util.NoCurrencyAvailableException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exchange implements ExchangeRepository {
    Map<Currency, BigDecimal> currencyAvailables;

    public Exchange() { currencyAvailables = new HashMap<>(); }

    public void addCurrency(Currency currency, BigDecimal amount) {
        currencyAvailables.put(currency, amount);
    }
    public void removeCurrency(Currency currency) {
        currencyAvailables.remove(currency);
    }
    public Map<Currency, BigDecimal> getCurrencies() {
        return currencyAvailables;
    }
    public int buyCurrency(Currency currency, BigDecimal amount) {
        if (currencyAvailables.containsKey(currency)) {
            if (currencyAvailables.get(currency).compareTo(amount) == 0) {
                currencyAvailables.remove(currency);
                return 1;
            } else if (currencyAvailables.get(currency).compareTo(amount) >= 0) {
                currencyAvailables.put(currency, currencyAvailables.get(currency).subtract(amount));
                return 0;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }
}
