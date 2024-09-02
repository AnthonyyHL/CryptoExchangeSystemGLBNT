package com.globant.application.port.in;

import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.orders.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface PlaceSellOrderUC {
    void createSellOrder(Currency crypto, BigDecimal amount, BigDecimal minimumPrice);
    Map<Currency, TreeMap<BigDecimal, List<Order>>> getSellOrders();
    Map<Currency, TreeMap<BigDecimal, List<Order>>> getSellOrdersByUsername(String username);
}
