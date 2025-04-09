package com.restaurant.entities;

import com.restaurant.exceptions.InsufficientStockException;
import com.restaurant.exceptions.InvalidStatusTransitionException;
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
    private StatusType status;
    private List<DishOrder> dishOrders = new ArrayList<>();
    private List<OrderStatus> statusHistory;

    public Order() {
        this.status = StatusType.CREATED;
    }

    public Order(int orderId, String reference, LocalDateTime createdAt, StatusType status, List<DishOrder> dishOrders, List<OrderStatus> statusHistory) {
        this.orderId = orderId;
        this.reference = reference;
        this.createdAt = createdAt;
        this.status = status;
        this.dishOrders = dishOrders;
        this.statusHistory = statusHistory;
    }

    public StatusType getActualStatus() {
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

    public void confirmOrder() {
        if (this.status != StatusType.CREATED) {
            throw new InvalidStatusTransitionException(
                    "Cannot confirm order from current status: " + this.status);
        }

        checkStockAvailability();

        this.status = StatusType.CONFIRMED;
        this.statusHistory.add(new OrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now()));
    }

    private void checkStockAvailability() {
        List<String> missingIngredients = new ArrayList<>();

        for (DishOrder dishOrder : dishOrders) {
            Dish dish = dishOrder.getDish();
            double availableQuantity = dish.getAvailableQuantity(LocalDateTime.now());

            if (availableQuantity < dishOrder.getQuantity()) {
                missingIngredients.add(String.format(
                        "%s (besoin: %d, disponible: %.2f)",
                        dish.getName(), dishOrder.getQuantity(), availableQuantity));
            }
        }

        if (!missingIngredients.isEmpty()) {
            throw new InsufficientStockException(
                    "Stock insuffisant pour: " + String.join(", ", missingIngredients));
        }
    }
}