package com.restaurant.dao;

import com.restaurant.entities.OrderStatus;

import java.util.List;

public interface OrderStatusDAO {
    List<OrderStatus> findByOrderId(int orderId);

    OrderStatus save(OrderStatus orderStatus, int orderId);
}
