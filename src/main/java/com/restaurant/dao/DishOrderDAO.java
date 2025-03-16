package com.restaurant.dao;

import com.restaurant.entities.DishOrder;
import com.restaurant.entities.StatusType;

import java.util.List;

public interface DishOrderDAO {
    DishOrder findById(int id);
    List<DishOrder> findByOrderId(int orderId);
    DishOrder save(DishOrder dishOrder);
    void updateStatus(int dishOrderId, StatusType status);
}