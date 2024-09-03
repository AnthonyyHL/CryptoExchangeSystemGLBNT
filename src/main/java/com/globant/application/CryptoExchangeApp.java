package com.globant.application;

import com.globant.adapters.console.ConsoleAdapter;
import com.globant.application.config.UsersLoader;
import com.globant.application.port.in.*;
import com.globant.application.port.out.ExchangeRepository;
import com.globant.application.port.out.OrderBookRepository;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.*;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.repositories.OrderBook;
import com.globant.domain.repositories.UserManager;

public class CryptoExchangeApp {
    public static void boot(){
        UserRepository userRepository = new UserManager();
        ExchangeRepository exchange = new Exchange();
        OrderBookRepository orderBook = new OrderBook();

        UsersLoader usersLoader = new UsersLoader(userRepository);
        usersLoader.loadUsers();

        UserRegistrationUC userRegistrationUC = new UserRegistrationUCImpl(userRepository);
        UserLoginUC userLoginUC = new UserLoginUCImpl(userRepository);
        UserLogoutUC userLogoutUC = new UserLogoutUCImpl(userRepository);
        InitializeCurrencyPricesUC initializeCurrencyPricesUC = new InitializeCurrencyPricesUCImpl(exchange);
        initializeCurrencyPricesUC.loadCurrencies(); //Cargar instancias de monedas disponibles en todo el sistema
        initializeCurrencyPricesUC.loadFiatOnSystem();
        initializeCurrencyPricesUC.loadCurrencyOnExchange();
        DepositMoneyUC depositMoneyUC = new DepositMoneyUCImpl();
        ViewWalletBalanceUC viewWalletBalanceUC = new ViewWalletBalanceUCImpl();
        BuyFromExchangeUC buyFromExchangeUC = new BuyFromExchangeUCImpl(exchange);
        ViewTransactionHistoryUC viewTransactionHistoryUC = new ViewTransactionHistoryUCImpl();
        PlaceBuyOrderUC placeBuyOrderUC = new PlaceBuyOrderUCImpl(orderBook);
        PlaceSellOrderUC placeSellOrderUC = new PlaceSellOrderUCImpl(orderBook);

        Currency.setReferenceCurrency(Currency.getInstance("USD"));

        ConsoleAdapter consoleAdapter = new ConsoleAdapter(
                userRegistrationUC,
                userLoginUC,
                userLogoutUC,
                initializeCurrencyPricesUC,
                depositMoneyUC,
                viewWalletBalanceUC,
                buyFromExchangeUC,
                viewTransactionHistoryUC,
                placeBuyOrderUC,
                placeSellOrderUC
        );
        consoleAdapter.boot();
    }
    public static void main(String[] args) {
        boot();
    }
}
