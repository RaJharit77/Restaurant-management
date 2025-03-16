package com.restaurant;

import com.restaurant.dao.*;
import com.restaurant.db.DataSource;
import com.restaurant.entities.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDishOrderTest {
    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;
    private DishOrderDAO dishOrderDAO;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new DataSource();
        orderDAO = new OrderDAOImpl(dataSource);
        dishOrderDAO = new DishOrderDAOImpl(dataSource);
        orderStatusDAO = new OrderStatusDAOImpl(dataSource);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM Dish_Order");
            statement.execute("DELETE FROM \"Order\"");
            statement.execute("DELETE FROM Order_Status");
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
        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);

        Order order = new Order();
        order.setReference("ORDER-003");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setOrder(savedOrder);
        dishOrder.setQuantity(2);
        dishOrder.setStatus(StatusType.CREATED);

        DishOrder savedDishOrder = dishOrderDAO.save(dishOrder);
        assertNotNull(savedDishOrder.getDishOrderId());
        assertEquals(2, savedDishOrder.getQuantity());
        assertEquals(StatusType.CREATED, savedDishOrder.getStatus());
    }

    @Test
    void testSaveOrderStatus() {
        Order order = new Order();
        order.setReference("ORDER-001");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        assertNotNull(savedOrder.getOrderId());

        OrderStatus orderStatus = new OrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now());

        OrderStatus savedOrderStatus = orderStatusDAO.save(orderStatus, savedOrder.getOrderId());
        assertNotNull(savedOrderStatus.getOrderStatusId());
        assertEquals(StatusType.CONFIRMED, savedOrderStatus.getStatus());
    }

    @Test
    void testFindOrderStatusByOrderId() {
        Order order = new Order();
        order.setReference("ORDER-002");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(StatusType.CREATED);

        Order savedOrder = orderDAO.save(order);
        assertNotNull(savedOrder.getOrderId());

        OrderStatus orderStatus1 = new OrderStatus(0, StatusType.CREATED, LocalDateTime.now());
        OrderStatus orderStatus2 = new OrderStatus(0, StatusType.CONFIRMED, LocalDateTime.now().plusMinutes(5));
        orderStatusDAO.save(orderStatus1, savedOrder.getOrderId());
        orderStatusDAO.save(orderStatus2, savedOrder.getOrderId());

        List<OrderStatus> orderStatuses = orderStatusDAO.findByOrderId(savedOrder.getOrderId());
        assertNotNull(orderStatuses);
        assertEquals(2, orderStatuses.size());
        assertEquals(StatusType.CREATED, orderStatuses.get(0).getStatus());
        assertEquals(StatusType.CONFIRMED, orderStatuses.get(1).getStatus());
    }
}