package com.restaurant.entities;

import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Order {
    private int orderId;
    private String reference;
    private LocalDateTime createdAt;
    private OrderStatus status;
    @Getter
    private List<DishOrder> dishOrders = new ArrayList<>();

    public Order() {
        this.status = OrderStatus.CREATED;
    }

    public Order(int orderId, String reference, LocalDateTime createdAt, OrderStatus status, List<DishOrder> dishOrders) {
        this.orderId = orderId;
        this.reference = reference;
        this.createdAt = createdAt;
        this.status = status;
        this.dishOrders = dishOrders;
    }

    public OrderStatus getActualStatus() {
        return status;
    }

    public void addDishOrder(DishOrder dishOrder) {
        dishOrder.setOrder(this);
        this.dishOrders.add(dishOrder);
    }

    public double getTotalAmount() {
        return dishOrders.stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity())
                .sum();
    }
}