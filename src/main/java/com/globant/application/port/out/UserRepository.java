package com.globant.application.port.out;

import com.globant.domain.entities.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();
    User getByUsername(String username);
    User save(User user);
    boolean usernameAlreadyExists(String username);
    boolean emailAlreadyExists(String email);
}
