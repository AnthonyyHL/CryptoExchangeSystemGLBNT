package com.globant.application.port.out;

import com.globant.domain.entities.Order;

import java.util.List;

public interface OrderBookRepository {
    void addOrder(Order order);
    void removeOrder(String orderId);
    void updateOrder(String orderId, Order order);
    Order getOrderById(String orderId);
    List<Order> getOrders();
}
