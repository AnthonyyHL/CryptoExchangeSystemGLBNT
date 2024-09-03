package com.globant.domain.repositories;

import com.globant.domain.entities.User;
import com.globant.domain.util.UserAuthException;

public class ActiveUser {
    private static ActiveUser instance;
    private User activeUser;

    private ActiveUser() {}

    public static ActiveUser getInstance() {
        if (instance == null)
            instance = new ActiveUser();
        return instance;
    }

    public void setActiveUser(User user) {
        if (activeUser != null)
            throw new UserAuthException("There is a user already logged in. Log out first.");
        activeUser = user;
    }

    public void logOutActiveUser(){
        activeUser = null;
    }

    public User getActiveUser() {
        return activeUser;
    }
}
