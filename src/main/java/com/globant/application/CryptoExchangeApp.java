package com.globant.application;

import com.globant.adapters.console.ConsoleAdapter;
import com.globant.application.config.UsersLoader;
import com.globant.application.port.in.InitializeCurrencyPricesUC;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.InitializeCurrencyPricesUCImpl;
import com.globant.application.usecases.UserLoginUCImpl;
import com.globant.application.usecases.UserRegistrationUCImpl;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.CurrencyFactory;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.repositories.UserManager;

import java.math.BigDecimal;

public class CryptoExchangeApp {
    public static void boot(){
        Currency.setReferenceCurrency(Currency.getInstance("USD"));

        UserRepository userRepository = new UserManager();
        Exchange exchange = new Exchange();

        UsersLoader usersLoader = new UsersLoader(userRepository);
        usersLoader.loadUsers();

        UserRegistrationUCImpl userRegistrationUC = new UserRegistrationUCImpl(userRepository);
        UserLoginUCImpl userLoginUC = new UserLoginUCImpl(userRepository);
        InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC = new InitializeCurrencyPricesUCImpl(exchange);
        initializeCurrencyPricesUC.loadCurrencies(); //Cargar instancias de monedas disponibles en todo el sistema
        initializeCurrencyPricesUC.loadCurrencyOnExchange();


        ConsoleAdapter consoleAdapter = new ConsoleAdapter(
                userRegistrationUC,
                userLoginUC,
                initializeCurrencyPricesUC
        );
        consoleAdapter.boot();
    }
    public static void main(String[] args) {
        boot();
    }
}
