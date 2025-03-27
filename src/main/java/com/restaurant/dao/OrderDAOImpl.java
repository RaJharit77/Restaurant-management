package com.restaurant.dao;

import com.restaurant.entities.*;
import com.restaurant.db.DataSource;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {
    private final DataSource dataSource;

    public OrderDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM \"Order\""; 
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setReference(resultSet.getString("reference"));
                order.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                order.setStatus(StatusType.valueOf(resultSet.getString("status")));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all orders", e);
        }
        return orders;
    }

    @Override
    public Order findById(int id) {
        String query = "SELECT * FROM \"Order\" WHERE order_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setReference(resultSet.getString("reference"));
                order.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                order.setStatus(StatusType.valueOf(resultSet.getString("status")));
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving order", e);
        }
        return null;
    }

    @Override
    public Order findByReference(String reference) {
        String query = "SELECT * FROM \"Order\" WHERE reference = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reference);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setReference(resultSet.getString("reference"));
                order.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                order.setStatus(StatusType.valueOf(resultSet.getString("status")));
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving order by reference", e);
        }
        return null;
    }

    @Override
    public Order save(Order order) {
        Order existingOrder = findByReference(order.getReference());
        if (existingOrder != null) {
            throw new RuntimeException("La référence de commande existe déjà : " + order.getReference());
        }

        String query = "INSERT INTO \"Order\" (reference, created_at, status) VALUES (?, ?, ?::status_type) RETURNING order_id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, order.getReference());
            statement.setTimestamp(2, Timestamp.valueOf(order.getCreatedAt()));
            statement.setString(3, order.getStatus().name());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order.setOrderId(resultSet.getInt("order_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving order", e);
        }
        return order;
    }

    @Override
    public void updateStatus(int orderId, StatusType status) {
        String query = "UPDATE \"Order\" SET status = ?::status_type WHERE order_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status.name());
            statement.setInt(2, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order status", e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM \"Order\" WHERE order_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }
}