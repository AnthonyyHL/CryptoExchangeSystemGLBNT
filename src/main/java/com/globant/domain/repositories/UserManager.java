package com.globant.domain.repositories;

import com.globant.application.port.out.UserRepository;
import com.globant.domain.entities.User;
import com.globant.domain.util.UserAuthException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager implements UserRepository {
    Map<String, User> users;
    Map<String, String> emailToUsername;
    Map<String, String> usernameToUsername;
    ActiveUser activeUser;

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
    public User save(User user) {
        User userSaved = users.put(user.getUsername(), user);
        emailToUsername.put(user.getEmail(), user.getUsername());
        usernameToUsername.put(user.getUsername(), user.getUsername());
        return userSaved;
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

    @Override
    public boolean passwordMatches(String email, String password) {
        String username = emailToUsername.get(email);
        return users.get(username).getPassword().equals(password);
    }
}