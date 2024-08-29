package com.globant.domain.entities;

import com.globant.domain.repositories.Wallet;
import com.globant.domain.util.MakeId;

import java.util.List;

public class User {
    static int accountNumberId = 1;
    private final String accountId;
    protected String username;
    protected String email;
    protected transient String password;
    private List<Transaction> transactions;
    private final Wallet wallet;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.wallet = new Wallet();
        accountId = MakeId.makeIdNumber(accountNumberId);
        accountNumberId++;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

    public Wallet getWallet() {
        return wallet;
    }

    public String toString() {
        return "User{" +
                "accountId='" + accountId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}