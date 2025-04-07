package com.restaurant.services;

import com.restaurant.dao.*;
import com.restaurant.entities.*;
import com.restaurant.exceptions.InsufficientStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final DishOrderDAO dishOrderDAO;
    private final OrderStatusDAO orderStatusDAO;
    private final DishOrderStatusDAO dishOrderStatusDAO;
    private final DishDAO dishDAO;
    private final IngredientDAO ingredientDAO;

    public OrderService(OrderDAO orderDAO, DishOrderDAO dishOrderDAO,
                        OrderStatusDAO orderStatusDAO, DishOrderStatusDAO dishOrderStatusDAO,
                        DishDAO dishDAO, IngredientDAO ingredientDAO) {
        this.orderDAO = orderDAO;
        this.dishOrderDAO = dishOrderDAO;
        this.orderStatusDAO = orderStatusDAO;
        this.dishOrderStatusDAO = dishOrderStatusDAO;
        this.dishDAO = dishDAO;
        this.ingredientDAO = ingredientDAO;
    }

    @Transactional
    public Order createOrder() {
        Order order = new Order();
        order.setReference(generateOrderReference());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        return orderDAO.save(order);
    }

    @Transactional
    public Order addDishToOrder(int orderId, int dishId, int quantity) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (order.getStatus() != StatusType.CREATED) {
            throw new RuntimeException("Cannot modify order after confirmation");
        }

        Dish dish = dishDAO.findById(dishId);
        if (dish == null) {
            throw new RuntimeException("Dish not found");
        }

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setQuantity(quantity);
        dishOrder.setStatus(StatusType.CREATED);

        dishOrder = dishOrderDAO.save(dishOrder);
        dishOrderStatusDAO.save(new DishOrderStatus(0, StatusType.CREATED, LocalDateTime.now()),
                dishOrder.getDishOrderId());

        return orderDAO.findById(orderId);
    }

    @Transactional
    public Order confirmOrder(int orderId) throws InsufficientStockException {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (order.getStatus() != StatusType.CREATED) {
            throw new RuntimeException("Order already confirmed or processed");
        }

        // Check stock availability for all dishes in the order
        for (DishOrder dishOrder : dishOrderDAO.findByOrderId(orderId)) {
            checkStockAvailability(dishOrder);
        }

        // Update order status
        order.setStatus(StatusType.CONFIRMED);
        orderDAO.updateStatus(orderId, StatusType.CONFIRMED);
        orderStatusDAO.save(new OrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now()), orderId);

        // Update all dish orders status
        for (DishOrder dishOrder : dishOrderDAO.findByOrderId(orderId)) {
            dishOrderDAO.updateStatus(dishOrder.getDishOrderId(), StatusType.CONFIRMED);
            dishOrderStatusDAO.save(
                    new DishOrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now()),
                    dishOrder.getDishOrderId()
            );
        }

        return orderDAO.findById(orderId);
    }

    private void checkStockAvailability(DishOrder dishOrder) throws InsufficientStockException {
        Dish dish = dishOrder.getDish();
        int quantity = dishOrder.getQuantity();

        for (Ingredient ingredient : dish.getIngredients()) {
            double required = ingredient.getRequiredQuantity() * quantity;
            double available = ingredient.getAvailableQuantity(LocalDateTime.now());

            if (available < required) {
                throw new InsufficientStockException(
                        String.format("Insufficient %s. Required: %.2f %s, Available: %.2f %s",
                                ingredient.getName(),
                                required,
                                ingredient.getUnit(),
                                available,
                                ingredient.getUnit())
                );
            }
        }
    }

    @Transactional
    public Order startPreparation(int orderId) {
        Order order = orderDAO.findById(orderId);
        validateOrderForPreparation(order);

        order.setStatus(StatusType.IN_PREPARATION);
        orderDAO.updateStatus(orderId, StatusType.IN_PREPARATION);
        orderStatusDAO.save(new OrderStatus(0, StatusType.IN_PREPARATION, LocalDateTime.now()), orderId);

        for (DishOrder dishOrder : dishOrderDAO.findByOrderId(orderId)) {
            dishOrderDAO.updateStatus(dishOrder.getDishOrderId(), StatusType.IN_PREPARATION);
            dishOrderStatusDAO.save(
                    new DishOrderStatus(0, StatusType.IN_PREPARATION, LocalDateTime.now()),
                    dishOrder.getDishOrderId()
            );
        }

        return orderDAO.findById(orderId);
    }

    @Transactional
    public Order completeDishOrder(int dishOrderId) {
        DishOrder dishOrder = dishOrderDAO.findById(dishOrderId);
        validateDishOrderForCompletion(dishOrder);

        dishOrderDAO.updateStatus(dishOrderId, StatusType.COMPLETED);
        dishOrderStatusDAO.save(
                new DishOrderStatus(0, StatusType.COMPLETED, LocalDateTime.now()),
                dishOrderId
        );

        Order order = checkIfAllDishesCompleted(dishOrder.getOrder().getOrderId());
        return orderDAO.findById(order.getOrderId());
    }

    @Transactional
    public Order serveDishOrder(int dishOrderId) {
        DishOrder dishOrder = dishOrderDAO.findById(dishOrderId);
        validateDishOrderForServing(dishOrder);

        dishOrderDAO.updateStatus(dishOrderId, StatusType.SERVED);
        dishOrderStatusDAO.save(
                new DishOrderStatus(0, StatusType.SERVED, LocalDateTime.now()),
                dishOrderId
        );

        Order order = checkIfAllDishesServed(dishOrder.getOrder().getOrderId());
        return orderDAO.findById(order.getOrderId());
    }

    private void validateOrderForPreparation(Order order) {
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        if (order.getStatus() != StatusType.CONFIRMED) {
            throw new RuntimeException("Order must be confirmed before preparation");
        }
    }

    private void validateDishOrderForCompletion(DishOrder dishOrder) {
        if (dishOrder == null) {
            throw new RuntimeException("Dish order not found");
        }
        if (dishOrder.getStatus() != StatusType.IN_PREPARATION) {
            throw new RuntimeException("Dish must be in preparation to be completed");
        }
    }

    private void validateDishOrderForServing(DishOrder dishOrder) {
        if (dishOrder == null) {
            throw new RuntimeException("Dish order not found");
        }
        if (dishOrder.getStatus() != StatusType.COMPLETED) {
            throw new RuntimeException("Dish must be completed to be served");
        }
    }

    private Order checkIfAllDishesCompleted(int orderId) {
        List<DishOrder> dishOrders = dishOrderDAO.findByOrderId(orderId);
        boolean allCompleted = dishOrders.stream()
                .allMatch(d -> d.getStatus() == StatusType.COMPLETED);

        if (allCompleted) {
            orderDAO.updateStatus(orderId, StatusType.COMPLETED);
            orderStatusDAO.save(
                    new OrderStatus(0, StatusType.COMPLETED, LocalDateTime.now()),
                    orderId
            );
        }
        return orderDAO.findById(orderId);
    }

    private Order checkIfAllDishesServed(int orderId) {
        List<DishOrder> dishOrders = dishOrderDAO.findByOrderId(orderId);
        boolean allServed = dishOrders.stream()
                .allMatch(d -> d.getStatus() == StatusType.SERVED);

        if (allServed) {
            orderDAO.updateStatus(orderId, StatusType.SERVED);
            orderStatusDAO.save(
                    new OrderStatus(0, StatusType.SERVED, LocalDateTime.now()),
                    orderId
            );
        }
        return orderDAO.findById(orderId);
    }

    public Order getOrderById(int orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        List<DishOrder> dishOrders = dishOrderDAO.findByOrderId(orderId);
        for (DishOrder dishOrder : dishOrders) {
            List<DishOrderStatus> statusHistory = dishOrderStatusDAO.findByDishOrderId(dishOrder.getDishOrderId());
            dishOrder.setStatusHistory(statusHistory);
        }

        order.setDishOrders(dishOrders);
        List<OrderStatus> statusHistory = orderStatusDAO.findByOrderId(orderId);
        // Note: Order n'a pas de champ statusHistory dans l'entité de base, vous pourriez l'ajouter si nécessaire

        return order;
    }

    public List<OrderStatus> getOrderStatusHistory(int orderId) {
        return orderStatusDAO.findByOrderId(orderId);
    }

    public List<DishOrderStatus> getDishOrderStatusHistory(int dishOrderId) {
        return dishOrderStatusDAO.findByDishOrderId(dishOrderId);
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderDAO.getAll();
        for (Order order : orders) {
            List<DishOrder> dishOrders = dishOrderDAO.findByOrderId(order.getOrderId());
            order.setDishOrders(dishOrders);
        }
        return orders;
    }

    private String generateOrderReference() {
        return "CMD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}