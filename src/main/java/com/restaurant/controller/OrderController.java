package com.restaurant.controller;

import com.restaurant.entities.*;
import com.restaurant.exceptions.InsufficientStockException;
import com.restaurant.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder() {
        return ResponseEntity.ok(orderService.createOrder());
    }

    @PostMapping("/{orderId}/dishes")
    public ResponseEntity<Order> addDishToOrder(
            @PathVariable int orderId,
            @RequestParam int dishId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(orderService.addDishToOrder(orderId, dishId, quantity));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable int orderId) {
        try {
            return ResponseEntity.ok(orderService.confirmOrder(orderId));
        } catch (InsufficientStockException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable int orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/start-preparation")
    public ResponseEntity<Order> startPreparation(@PathVariable int orderId) {
        try {
            return ResponseEntity.ok(orderService.startPreparation(orderId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/dish-orders/{dishOrderId}/complete")
    public ResponseEntity<Order> completeDishOrder(@PathVariable int dishOrderId) {
        try {
            return ResponseEntity.ok(orderService.completeDishOrder(dishOrderId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/dish-orders/{dishOrderId}/serve")
    public ResponseEntity<Order> serveDishOrder(@PathVariable int dishOrderId) {
        try {
            return ResponseEntity.ok(orderService.serveDishOrder(dishOrderId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{orderId}/status-history")
    public ResponseEntity<List<OrderStatus>> getOrderStatusHistory(@PathVariable int orderId) {
        return ResponseEntity.ok(orderService.getOrderStatusHistory(orderId));
    }

    @GetMapping("/dish-orders/{dishOrderId}/status-history")
    public ResponseEntity<List<DishOrderStatus>> getDishOrderStatusHistory(@PathVariable int dishOrderId) {
        return ResponseEntity.ok(orderService.getDishOrderStatusHistory(dishOrderId));
    }
}