package com.globant.application;

import com.globant.adapters.persistence.InMemory;
import com.globant.application.usecases.UserRegistrationUCImpl;

public class CryptoExchangeApp {
    public static void boot(){
        UserRegistrationUCImpl userRegistrationUC = new UserRegistrationUCImpl();
        InMemory inMemory = new InMemory(userRegistrationUC);
    }
    public static void main(String[] args) {
        boot();
    }
}
