package com.globant.application.usecases;

import com.globant.application.port.in.BuyFromExchangeUC;
import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.util.NoCurrencyAvailableException;

import java.math.BigDecimal;
import java.util.List;

public class BuyFromExchangeUCImpl implements BuyFromExchangeUC {
    Exchange exchange;
    User activeUser = ActiveUser.getInstance().getActiveUser();
    public BuyFromExchangeUCImpl(Exchange exchange) {
        this.exchange = exchange;
    }

    public void buyCurrency(int option, BigDecimal amount) {
        List<Currency> currencies = exchange.getCurrencies().keySet().stream().toList();
        Currency currency = currencies.get(option - 1);

        int result = exchange.buyCurrency(currency, amount);
        updateUserWallet(currency, amount);
        if (result == 1) {
            throw new NoCurrencyAvailableException("Not enough currency available");
        } else if (result == -1) {
            throw new NoCurrencyAvailableException("Currency not available");
        } else {
            throw new NoCurrencyAvailableException("Currency bought successfully");
        }
    }

    public void updateUserWallet(Currency currency, BigDecimal amount) {
        activeUser.getWallet().addCryptocurrency(currency, amount);
    }
}
