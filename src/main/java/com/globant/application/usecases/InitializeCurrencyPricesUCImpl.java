package com.globant.application.usecases;

import com.globant.application.port.in.InitializeCurrencyPricesUC;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.CurrencyFactory;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.util.NoCurrencyAvailableException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class InitializeCurrencyPricesUCImpl implements InitializeCurrencyPricesUC {
    Exchange exchange;
    public InitializeCurrencyPricesUCImpl(Exchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void loadCurrencies() {
        CurrencyFactory.createCurrency("crypto", "BTC", "Bitcoin", new BigDecimal("50000"));
        CurrencyFactory.createCurrency("crypto", "ETH", "Ethereum", new BigDecimal("3000"));
    }

    public void loadCurrencyOnExchange() {
        Currency bitcoin = Currency.getInstance("BTC");
        Currency ethereum = Currency.getInstance("ETH");

        exchange.addCurrency(bitcoin, new BigDecimal("100"));
        exchange.addCurrency(ethereum, new BigDecimal("200"));
    }

    public Map<Currency, BigDecimal> getCurrencyAvailables() {
        return exchange.getCurrencies();
    }
}