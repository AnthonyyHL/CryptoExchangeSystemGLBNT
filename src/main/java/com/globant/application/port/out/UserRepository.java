package com.globant.application.port.out;

import com.globant.domain.entities.User;

public interface UserRepository {
    void setActiveUser(User user);
    void removeActiveUser();
    User getByUsername(String username);
    User getByEmail(String email);
    void save(User user);
    boolean usernameAlreadyExists(String username);
    boolean emailAlreadyExists(String email);
    boolean passwordMatches(String username, String password);
}
