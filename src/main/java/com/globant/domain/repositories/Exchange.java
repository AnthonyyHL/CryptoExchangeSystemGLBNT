package com.globant.domain.repositories;

import com.globant.application.port.out.ExchangeRepository;
import com.globant.domain.entities.currencies.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Exchange implements ExchangeRepository {
    private Map<Currency, BigDecimal> currencyAvailables;

    public Exchange() { currencyAvailables = new HashMap<>(); }

    @Override
    public void addCurrency(Currency currency, BigDecimal amount) {
        currencyAvailables.put(currency, amount);
    }
    @Override
    public void removeCurrency(Currency currency) {
        currencyAvailables.remove(currency);
    }
    @Override
    public Map<Currency, BigDecimal> getCurrencies() {
        return currencyAvailables;
    }
    @Override
    public int buyCurrency(Currency currency, BigDecimal amount) {
        BigDecimal userBalance = ActiveUser.getInstance().getActiveUser().getWallet().getBalance();
        if (userBalance.compareTo(amount) < 0) {
            return -2;
        }
        if (currencyAvailables.containsKey(currency)) {
            if (currencyAvailables.get(currency).compareTo(amount) == 0) {
                removeCurrency(currency);
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
