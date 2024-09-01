package com.globant.application.port.out;

import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.orders.Order;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface OrderBookRepository {
    Order createOrder(TradeType tradeType, Currency cryptoType, BigDecimal amount, BigDecimal price);
    void addOrder(Order order);
    void removeOrder(String orderId);
    void updateOrder(String orderId, Order order);
    Order getOrderById(String orderId);
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getBuyOrders();
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getSellOrders();
    void matchSeller(Order buyOrder);
    void matchBuyer(Order sellOrder);
    void match(Order sellOrder, Order buyOrder);
}
