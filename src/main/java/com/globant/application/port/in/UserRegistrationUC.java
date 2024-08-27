package com.globant.application.port.in;

public interface UserRegistrationUC {
    void registerUser(String name, String email, String password);
    void existsByUsername(String username);
    void existsByEmail(String email);
}
