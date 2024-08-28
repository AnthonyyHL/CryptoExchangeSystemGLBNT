package com.globant.application.port.out;

import com.globant.domain.entities.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();
    void setActiveUser(User user);
    void removeActiveUser();
    User getByUsername(String username);
    User getByEmail(String email);
    User save(User user);
    boolean usernameAlreadyExists(String username);
    boolean emailAlreadyExists(String email);
    boolean passwordMatches(String username, String password);
}
