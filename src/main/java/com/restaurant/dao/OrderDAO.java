package com.restaurant.dao;

import com.restaurant.entities.Order;
import com.restaurant.entities.OrderStatus;

import java.util.List;

public interface OrderDAO {
    List<Order> getAll();
    Order findById(int id);
    Order findByReference(String reference);
    Order save(Order order);
    void updateStatus(int orderId, OrderStatus status);
    void delete(int id);
}