package com.globant.application.usecases;

import com.globant.application.port.in.UserRegistrationUC;
import com.globant.application.port.out.UserRepository;
import com.globant.domain.entities.User;
import com.globant.domain.repositories.UserManager;
import com.globant.domain.util.UserCreationException;

public class UserRegistrationUCImpl implements UserRegistrationUC {
    UserRepository userRepository;
    @Override
    public void registerUser(String name, String email, String password) {
        User user = new User(name, email, password);
        userRepository = new UserManager();
        userRepository.save(user);
    }
    @Override
    public void existsByUsername(String username) {
        if (userRepository.usernameAlreadyExists(username))
            throw new UserCreationException("Username already exists. Try again with a different username.");
    }
    @Override
    public void existsByEmail(String email) {
        if (userRepository.emailAlreadyExists(email))
            throw new UserCreationException("Email already in use. Try again with a different email.");
    }
}
