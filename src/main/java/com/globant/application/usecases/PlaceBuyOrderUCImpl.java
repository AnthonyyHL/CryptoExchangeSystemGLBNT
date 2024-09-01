package com.globant.application.usecases;

import com.globant.application.port.in.PlaceBuyOrderUC;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.orders.BuyOrder;
import com.globant.domain.entities.orders.Order;
import com.globant.domain.repositories.OrderBook;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlaceBuyOrderUCImpl implements PlaceBuyOrderUC {
    private OrderBook orderBook;
    Map<Currency, TreeMap<BigDecimal, List<Order>>> buyOrders;

    public PlaceBuyOrderUCImpl(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void createBuyOrder(Currency crypto, BigDecimal amount, BigDecimal maximumPrice) {
        Order buyOrder = new BuyOrder(crypto, amount, maximumPrice);
        orderBook.matchSeller(buyOrder);
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getOrders() {
        buyOrders = orderBook.getBuyOrders();
        return buyOrders;
    }
}
