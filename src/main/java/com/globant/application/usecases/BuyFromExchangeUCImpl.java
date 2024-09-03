package com.globant.application.usecases;

import com.globant.application.port.in.BuyFromExchangeUC;
import com.globant.application.port.out.ExchangeRepository;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.repositories.Wallet;
import com.globant.domain.util.NoCurrencyAvailableException;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.List;

public class BuyFromExchangeUCImpl implements BuyFromExchangeUC {
    ExchangeRepository exchange;
    public BuyFromExchangeUCImpl(ExchangeRepository exchange) {
        this.exchange = exchange;
    }

    @Override
    public void buyCurrency(int option, BigDecimal amount) {
        User activeUser = ActiveUser.getInstance().getActiveUser();
        List<Currency> currencies = exchange.getCurrencies().keySet().stream().toList();
        Currency currency = currencies.get(option - 1);

        int result = exchange.buyCurrency(currency, amount);
        updateUserWallet(activeUser, currency, amount);

        Transaction transaction = activeUser.getWallet().makeTransaction(currency, amount, TradeType.BUY, "Exchange");
        activeUser.addTransaction(transaction);

        if (result == 1) {
            throw new NoCurrencyAvailableException("Not enough currency available");
        } else if (result == -1) {
            throw new NoCurrencyAvailableException("Currency not available");
        } else if (result == -2) {
            throw new NoCurrencyAvailableException("Not enough balance");
        } else {
            throw new NoCurrencyAvailableException("Currency bought successfully");
        }
    }
    @Override
    public void updateUserWallet(User activeUser, Currency currency, BigDecimal amount) {
        Wallet wallet = activeUser.getWallet();
        wallet.addCryptocurrency(currency, amount);
    }
}
