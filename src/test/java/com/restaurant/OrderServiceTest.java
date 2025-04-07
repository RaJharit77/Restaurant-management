package com.restaurant;

import com.restaurant.dao.*;
import com.restaurant.entities.*;
import com.restaurant.exceptions.InsufficientStockException;
import com.restaurant.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private DishDAO dishDAO;

    @Autowired
    private IngredientDAO ingredientDAO;

    @Autowired
    private StockMovementDAO stockMovementDAO;

    private Dish testDish;
    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testIngredient = new Ingredient();
        testIngredient.setName("Test Ingredient");
        testIngredient.setUnitPrice(10);
        testIngredient.setUnit(Unit.G);
        testIngredient.setUpdateDateTime(LocalDateTime.now());
        ingredientDAO.saveAll(List.of(testIngredient));

        StockMovement stockEntry = new StockMovement(
                0, testIngredient.getId(), MovementType.ENTRY, 1000, Unit.G, LocalDateTime.now()
        );
        stockMovementDAO.saveStockMovement(stockEntry);

        testDish = new Dish();
        testDish.setName("Test Dish");
        testDish.setUnitPrice(100);
        testDish.setIngredients(List.of(testIngredient));
        dishDAO.saveAll(List.of(testDish));
    }

    @Test
    void testCreateOrder() {
        Order order = orderService.createOrder();
        assertNotNull(order.getOrderId());
        assertEquals(StatusType.CREATED, order.getStatus());
        assertNotNull(order.getReference());
    }

    @Test
    void testAddDishToOrder() {
        Order order = orderService.createOrder();
        Order updatedOrder = orderService.addDishToOrder(order.getOrderId(), testDish.getId(), 2);

        assertEquals(1, updatedOrder.getDishOrders().size());
        assertEquals(2, updatedOrder.getDishOrders().get(0).getQuantity());
    }

    @Test
    void testConfirmOrderWithSufficientStock() {
        Order order = orderService.createOrder();
        orderService.addDishToOrder(order.getOrderId(), testDish.getId(), 1);

        try {
            Order confirmedOrder = orderService.confirmOrder(order.getOrderId());
            assertEquals(StatusType.CONFIRMED, confirmedOrder.getStatus());
        } catch (InsufficientStockException e) {
            fail("Should not throw InsufficientStockException");
        }
    }

    @Test
    void testConfirmOrderWithInsufficientStock() {
        Order order = orderService.createOrder();
        orderService.addDishToOrder(order.getOrderId(), testDish.getId(), 100);

        assertThrows(InsufficientStockException.class, () -> {
            orderService.confirmOrder(order.getOrderId());
        });
    }

    @Test
    void testOrderWorkflow() throws InsufficientStockException {
        Order order = orderService.createOrder();
        orderService.addDishToOrder(order.getOrderId(), testDish.getId(), 1);
        orderService.confirmOrder(order.getOrderId());

        Order inPreparation = orderService.startPreparation(order.getOrderId());
        assertEquals(StatusType.IN_PREPARATION, inPreparation.getStatus());

        DishOrder dishOrder = inPreparation.getDishOrders().get(0);
        Order completed = orderService.completeDishOrder(dishOrder.getDishOrderId());
        assertEquals(StatusType.COMPLETED, completed.getStatus());

        Order served = orderService.serveDishOrder(dishOrder.getDishOrderId());
        assertEquals(StatusType.SERVED, served.getStatus());
    }
}