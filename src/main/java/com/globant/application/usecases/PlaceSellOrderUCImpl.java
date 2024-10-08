package com.globant.application.usecases;

import com.globant.application.port.in.PlaceSellOrderUC;
import com.globant.application.port.out.OrderBookRepository;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.orders.Order;
import com.globant.domain.util.InvalidOrderException;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlaceSellOrderUCImpl implements PlaceSellOrderUC {
    private final OrderBookRepository orderBook;

    public PlaceSellOrderUCImpl(OrderBookRepository orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void createSellOrder(Currency crypto, BigDecimal amount, BigDecimal minimumPrice) {
        try {
            Order sellOrder = orderBook.createOrder(TradeType.SELL, crypto, amount, minimumPrice);
            orderBook.matchBuyer(sellOrder);
        } catch (InvalidOrderException e) {
            System.err.println(e.getMessage());
        }
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getSellOrders() {
        return orderBook.getSellOrders();
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getSellOrdersByUsername(String username) {
        return orderBook.getSellOrdersByUsername(username);
    }
}
