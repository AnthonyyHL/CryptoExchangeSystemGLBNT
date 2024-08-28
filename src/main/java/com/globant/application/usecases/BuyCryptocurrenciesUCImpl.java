package com.globant.application.usecases;

import com.globant.application.port.in.BuyCryptocurrenciesUC;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.util.NoCurrencyAvailableException;

import java.math.BigDecimal;
import java.util.List;

public class BuyCryptocurrenciesUCImpl implements BuyCryptocurrenciesUC {
    Exchange exchange;
    public BuyCryptocurrenciesUCImpl(Exchange exchange) {
        this.exchange = exchange;
    }

    public void buyCurrency(int option, BigDecimal amount) {
        List<Currency> currencies = exchange.getCurrencies().keySet().stream().toList();
        Currency currency = currencies.get(option - 1);

        int result = exchange.buyCurrency(currency, amount);
        if (result == 1) {
            throw new NoCurrencyAvailableException("Not enough currency available");
        } else if (result == -1) {
            throw new NoCurrencyAvailableException("Currency not available");
        } else {
            throw new NoCurrencyAvailableException("Currency bought successfully");
        }
    }
}
