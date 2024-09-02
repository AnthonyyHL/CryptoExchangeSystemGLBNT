package com.globant.application.usecases;

import com.globant.application.port.in.PlaceBuyOrderUC;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.orders.BuyOrder;
import com.globant.domain.entities.orders.Order;
import com.globant.domain.repositories.OrderBook;
import com.globant.domain.util.InvalidOrderException;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlaceBuyOrderUCImpl implements PlaceBuyOrderUC {
    private final OrderBook orderBook;
    private Map<Currency, TreeMap<BigDecimal, List<Order>>> buyOrders;

    public PlaceBuyOrderUCImpl(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void createBuyOrder(Currency crypto, BigDecimal amount, BigDecimal maximumPrice) {
        try {
            Order buyOrder = orderBook.createOrder(TradeType.BUY, crypto, amount, maximumPrice);
            orderBook.matchSeller(buyOrder);
        } catch (InvalidOrderException e) {
            System.err.println(e.getMessage());
        }
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getBuyOrders() {
        return orderBook.getBuyOrders();
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getBuyOrdersByUsername(String username) {
        return orderBook.getBuyOrdersByUsername(username);
    }
}
