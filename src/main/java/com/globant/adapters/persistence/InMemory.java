package com.globant.adapters.persistence;

import com.globant.application.usecases.UserRegistrationUCImpl;

public class InMemory {
    private UserRegistrationUCImpl userRegistrationUC;
    public InMemory(UserRegistrationUCImpl userRegistrationUC){
        userRegistrationUC = new UserRegistrationUCImpl();
    }
}
