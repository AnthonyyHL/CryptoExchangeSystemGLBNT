package com.globant.domain.repositories;

import com.globant.application.port.out.OrderBookRepository;
import com.globant.domain.entities.orders.Order;

import java.util.List;

public class OrderBook implements OrderBookRepository {
    private List<Order> orders;

    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }
    @Override
    public void removeOrder(String orderId) {
        orders.removeIf(order -> order.getOrderId().equals(orderId));
    }
    @Override
    public void updateOrder(String orderId, Order order) {
        orders.removeIf(o -> o.getOrderId().equals(orderId));
        orders.add(order);
    }
    @Override
    public Order getOrderById(String orderId) {
        return orders.stream().filter(order -> order.getOrderId().equals(orderId)).findFirst().orElse(null);
    }
    @Override
    public List<Order> getOrders() {
        return orders;
    }

}
