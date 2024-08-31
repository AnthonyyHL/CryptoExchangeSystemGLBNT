package com.globant.application;

import com.globant.adapters.console.ConsoleAdapter;
import com.globant.application.config.UsersLoader;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.*;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.repositories.UserManager;

public class CryptoExchangeApp {
    public static void boot(){
        UserRepository userRepository = new UserManager();
        Exchange exchange = new Exchange();

        UsersLoader usersLoader = new UsersLoader(userRepository);
        usersLoader.loadUsers();

        UserRegistrationUCImpl userRegistrationUC = new UserRegistrationUCImpl(userRepository);
        UserLoginUCImpl userLoginUC = new UserLoginUCImpl(userRepository);
        InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC = new InitializeCurrencyPricesUCImpl(exchange);
        initializeCurrencyPricesUC.loadCurrencies(); //Cargar instancias de monedas disponibles en todo el sistema
        initializeCurrencyPricesUC.loadCurrencyOnExchange();
        DepositMoneyUCImpl depositMoneyUC = new DepositMoneyUCImpl();
        ViewWalletBalanceUCImpl viewWalletBalanceUC = new ViewWalletBalanceUCImpl(exchange);
        BuyFromExchangeUCImpl buyFromExchangeUC = new BuyFromExchangeUCImpl(exchange);
        ViewTransactionHistoryUCImpl viewTransactionHistoryUC = new ViewTransactionHistoryUCImpl();

        Currency.setReferenceCurrency(Currency.getInstance("USD"));

        ConsoleAdapter consoleAdapter = new ConsoleAdapter(
                userRegistrationUC,
                userLoginUC,
                initializeCurrencyPricesUC,
                depositMoneyUC,
                viewWalletBalanceUC,
                buyFromExchangeUC,
                viewTransactionHistoryUC
        );
        consoleAdapter.boot();
    }
    public static void main(String[] args) {
        boot();
    }
}
