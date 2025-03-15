package com.restaurant;

import com.restaurant.dao.*;
import com.restaurant.db.DataSource;
import com.restaurant.entities.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class OrderDishOrderTest {
    private OrderDAO orderDAO;
    private DishOrderDAO dishOrderDAO;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new DataSource();
        orderDAO = new OrderDAOImpl(dataSource);
        dishOrderDAO = new DishOrderDAOImpl(dataSource);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM Dish_Order");
            statement.execute("DELETE FROM \"Order\"");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du nettoyage de la base de données", e);
        }
    }

    @Test
    void testCreateOrder() {
        Order order = new Order();
        order.setReference("ORDER-001");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        assertNotNull(savedOrder.getOrderId());
        assertEquals("ORDER-001", savedOrder.getReference());
        assertEquals(StatusType.CREATED, savedOrder.getStatus());
    }

    @Test
    void testFindById() {
        Order order = new Order();
        order.setReference("ORDER-004");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        Order existingOrder = orderDAO.findByReference(order.getReference());
        if (existingOrder != null) {
            throw new RuntimeException("La référence de commande existe déjà : " + order.getReference());
        }

        Order savedOrder = orderDAO.save(order);
        Order retrievedOrder = orderDAO.findById(savedOrder.getOrderId());

        assertNotNull(retrievedOrder);
        assertEquals("ORDER-004", retrievedOrder.getReference());
        assertEquals(StatusType.CREATED, retrievedOrder.getStatus());
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order();
        order.setReference("ORDER-002");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        orderDAO.updateStatus(savedOrder.getOrderId(), StatusType.CONFIRMED);

        Order updatedOrder = orderDAO.findById(savedOrder.getOrderId());
        assertEquals(StatusType.CONFIRMED, updatedOrder.getStatus());
    }

    @Test
    void testAddDishToOrder() {
        Order order = new Order();
        order.setReference("ORDER-003");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);

        Dish dish = new Dish();
        dish.setId(45);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setOrder(savedOrder);
        dishOrder.setQuantity(2);
        dishOrder.setStatus(DishStatus.CREATED);

        DishOrder savedDishOrder = dishOrderDAO.save(dishOrder);
        assertNotNull(savedDishOrder.getDishOrderId());
        assertEquals(2, savedDishOrder.getQuantity());
        assertEquals(DishStatus.CREATED, savedDishOrder.getStatus());
    }
}