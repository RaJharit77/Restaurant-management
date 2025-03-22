package com.restaurant.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DishOrder {
    private int dishOrderId;
    private Dish dish;
    private Order order;
    private int quantity;
    private StatusType status;
    private List<DishOrderStatus> statusHistory;

    public DishOrder() {
        this.status = StatusType.CREATED;
    }

    public DishOrder(int dishOrderId, Dish dish, Order order, int quantity, StatusType status, List<DishOrderStatus> statusHistory) {
        this.dishOrderId = dishOrderId;
        this.dish = dish;
        this.order = order;
        this.quantity = quantity;
        this.status = status;
        this.statusHistory = statusHistory;
    }

    public StatusType getActualStatus() {
        return status;
    }
}