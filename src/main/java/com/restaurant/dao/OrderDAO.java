package com.restaurant.dao;

import com.restaurant.entities.Order;
import com.restaurant.entities.StatusType;

import java.util.List;

public interface OrderDAO {
    List<Order> getAll();
    Order findById(int id);
    Order findByReference(String reference);
    Order save(Order order);
    void updateStatus(int orderId, StatusType status);
    void delete(int id);
}