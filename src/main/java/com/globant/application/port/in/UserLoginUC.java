package com.globant.application.port.in;

public interface UserLoginUC {
    void logInUser(String email, String password);
    void correctEmail(String email);
    void correctPassword(String email, String password);
}
