package com.globant.application.usecases;

import com.globant.application.port.in.UserLogoutUC;
import com.globant.domain.repositories.ActiveUser;

public class UserLogoutUCImpl implements UserLogoutUC {
    @Override
    public void logout() {
        ActiveUser.getInstance().logOutActiveUser();
    }
}