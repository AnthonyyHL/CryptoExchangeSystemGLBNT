package com.globant.application.usecases;

import com.globant.application.port.in.UserLogoutUC;
import com.globant.application.port.out.UserRepository;

public class UserLogoutUCImpl implements UserLogoutUC {
    UserRepository userRepository;

    public UserLogoutUCImpl(UserRepository userRepository) { this.userRepository = userRepository; }
    @Override
    public void logout() {
        userRepository.removeActiveUser();
    }
}