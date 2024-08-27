package com.globant.application.usecases;

import com.globant.application.port.in.UserLoginUC;
import com.globant.application.port.out.UserRepository;
import com.globant.domain.entities.User;
import com.globant.domain.util.UserAuthException;

public class UserLoginUCImpl implements UserLoginUC {
    UserRepository userRepository;

    public UserLoginUCImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void logInUser(String email, String password) {
        User user = userRepository.getByUsername(email);
        userRepository.setActiveUser(user);
    }

    @Override
    public void correctEmail(String email) {
        if (!userRepository.emailAlreadyExists(email))
            throw new UserAuthException("Email does not exist. Try again with a different email.");
    }

    @Override
    public void correctPassword(String email, String password) {
        if (!userRepository.passwordMatches(email, password))
            throw new UserAuthException("The password is incorrect. Try again.");
    }
}
