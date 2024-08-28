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

        System.out.println("\n");

        try {
            Crypto crypto = new Crypto("Bitcoin", new BigDecimal(1000));
            Order order = new SellOrder(crypto, new BigDecimal(10), new BigDecimal(2000));
            System.out.println(order);
            System.out.println("\n");
        } catch (InvalidOrderException e) {
            System.out.println(e.getMessage());
        }

        Crypto crypto1 = new Crypto("Bitcoin", new BigDecimal(1000));
        System.out.println(crypto1.getShorthandSymbol());
    }
}