package com.restaurant.entities;

import lombok.*;
import java.time.LocalDateTime;
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
    private DishStatus status;
    private List<StatusHistory> statusHistory;

    public DishOrder() {
        this.status = DishStatus.CREATED;
    }

    public DishOrder(int dishOrderId, Dish dish, Order order, int quantity, DishStatus status, List<StatusHistory> statusHistory) {
        this.dishOrderId = dishOrderId;
        this.dish = dish;
        this.order = order;
        this.quantity = quantity;
        this.status = status;
        this.statusHistory = statusHistory;
    }

    public DishStatus getActualStatus() {
        return status;
    }
}