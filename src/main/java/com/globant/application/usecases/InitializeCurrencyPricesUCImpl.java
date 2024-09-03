package com.globant.application.usecases;

import com.globant.application.port.in.InitializeCurrencyPricesUC;
import com.globant.application.port.out.ExchangeRepository;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.CurrencyFactory;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.util.NoCurrencyAvailableException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InitializeCurrencyPricesUCImpl implements InitializeCurrencyPricesUC {
    List<Fiat> fiatAvailables;
    private final ExchangeRepository exchange;
    public InitializeCurrencyPricesUCImpl(ExchangeRepository exchange) {
        this.exchange = exchange;
    }

    @Override
    public void loadCurrencies() {
        try {
            CurrencyFactory.createCurrency("crypto", "BTC", "Bitcoin", new BigDecimal("50000"));
            CurrencyFactory.createCurrency("crypto", "ETH", "Ethereum", new BigDecimal("3000"));

            CurrencyFactory.createCurrency("fiat", "USD", "Dollar", new BigDecimal("1"));
            CurrencyFactory.createCurrency("fiat", "EUR", "Euro", new BigDecimal("1.11"));
        } catch (NoCurrencyAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void loadCurrencyOnExchange() {
        Currency bitcoin = Currency.getInstance("BTC");
        Currency ethereum = Currency.getInstance("ETH");

        exchange.addCurrency(bitcoin, new BigDecimal("100"));
        exchange.addCurrency(ethereum, new BigDecimal("500"));
    }

    @Override
    public Map<Currency, BigDecimal> getCurrencyAvailables() {
        return exchange.getCurrencies();
    }
    @Override
    public void loadFiatOnSystem() {
        fiatAvailables = new ArrayList<>();
        fiatAvailables.add((Fiat)Currency.getInstance("USD"));
        fiatAvailables.add((Fiat)Currency.getInstance("EUR"));
    }
    @Override
    public List<Fiat> getFiatAvailables() { return fiatAvailables; }
}