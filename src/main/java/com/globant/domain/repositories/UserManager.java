package com.globant.domain.repositories;

import com.globant.application.port.out.UserRepository;
import com.globant.domain.entities.User;
import com.globant.domain.util.UserAuthException;

import java.util.HashMap;
import java.util.Map;

public class UserManager implements UserRepository {
    private Map<String, User> users;
    private Map<String, String> emailToUsername;
    private Map<String, String> usernameToUsername;
    private ActiveUser activeUser;

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
    public User getByEmail(String email) {
        String username = emailToUsername.get(email);
        return users.get(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
        emailToUsername.put(user.getEmail(), user.getUsername());
        usernameToUsername.put(user.getUsername(), user.getUsername());
    }

    @Override
    public void setActiveUser(User user) {
        activeUser = ActiveUser.getInstance();
        try {
            activeUser.setActiveUser(user);
        } catch (UserAuthException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void removeActiveUser() {
        activeUser.logOutActiveUser();
    }

    @Override
    public boolean usernameAlreadyExists(String username) {
        return users.containsKey(username);
    }

    @Override
    public boolean emailAlreadyExists(String email) {
        return emailToUsername.containsKey(email);
    }

    @Override
    public boolean passwordMatches(String email, String password) {
        String username = emailToUsername.get(email);
        return users.get(username).getPassword().equals(password);
    }
}