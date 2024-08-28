package com.globant;

import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.orders.Order;
import com.globant.domain.entities.orders.SellOrder;
import com.globant.domain.util.InvalidOrderException;

import java.math.BigDecimal;

public class Main {
    static int numberId = 10;
    public static void main(String[] args) {
        // TESTS
        User user = new User("anthleon", "anthleon@gmail.com", "12345a6");
        System.out.println("000" + numberId);
        System.out.println(user.getAccountId());
    }
}