package com.globant.domain.repositories;

import com.globant.application.port.out.UserRepository;
import com.globant.domain.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager implements UserRepository {
    Map<String, User> users;
    Map<String, String> emailToUsername;
    Map<String, String> usernameToUsername;

    public UserManager() {
        this.users = new HashMap<>();
        this.emailToUsername = new HashMap<>();
        this.usernameToUsername = new HashMap<>();
    }

    @Override
    public User getByUsername(String username) {
        return users.get(username);
    }

    @Override
    public User save(User user) {
        User userSaved = users.put(user.getUsername(), user);
        emailToUsername.put(user.getEmail(), user.getUsername());
        usernameToUsername.put(user.getUsername(), user.getUsername());
        return userSaved;
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public boolean usernameAlreadyExists(String username) {
        return users.containsKey(username);
    }

    @Override
    public boolean emailAlreadyExists(String email) {
        return emailToUsername.containsKey(email);
    }

}
