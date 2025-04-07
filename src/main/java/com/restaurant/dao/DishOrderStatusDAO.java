package com.restaurant.dao;

import com.restaurant.entities.DishOrderStatus;

import java.util.List;

public interface DishOrderStatusDAO {
    DishOrderStatus save(DishOrderStatus dishOrderStatus, int dishOrderId);
    List<DishOrderStatus> findByDishOrderId(int dishOrderId);
}
