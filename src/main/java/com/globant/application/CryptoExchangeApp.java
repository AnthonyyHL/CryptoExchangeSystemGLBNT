package com.globant.application;

import com.globant.adapters.console.ConsoleAdapter;
import com.globant.application.config.UsersLoader;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.UserLoginUCImpl;
import com.globant.application.usecases.UserRegistrationUCImpl;
import com.globant.domain.repositories.UserManager;

public class CryptoExchangeApp {
    public static void boot(){
        UserRepository userRepository = new UserManager();

        UsersLoader usersLoader = new UsersLoader(userRepository);
        usersLoader.loadUsers();

        UserRegistrationUCImpl userRegistrationUC = new UserRegistrationUCImpl(userRepository);
        UserLoginUCImpl userLoginUC = new UserLoginUCImpl(userRepository);

        ConsoleAdapter consoleAdapter = new ConsoleAdapter(userRegistrationUC, userLoginUC);
        consoleAdapter.boot();
    }
    public static void main(String[] args) {
        boot();
    }
}
