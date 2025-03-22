package com.restaurant.dao;

import com.restaurant.db.DataSource;
import com.restaurant.entities.MovementType;
import com.restaurant.entities.StockMovement;
import com.restaurant.entities.Unit;

import java.sql.*;
import java.util.*;

public class StockMovementImpl implements StockMovementDAO {
    private final DataSource dataSource;

    public StockMovementImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveStockMovement(StockMovement stockMovement) {
        String query = "INSERT INTO Stock_Movement (ingredient_id, movement_type, quantity, unit, movement_date) VALUES (?, ?::movement_type, ?, ?::unit_type, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, stockMovement.getIngredientId());
            statement.setString(2, stockMovement.getMovementType().name());
            statement.setDouble(3, stockMovement.getQuantity());
            statement.setString(4, stockMovement.getUnit().name());
            statement.setTimestamp(5, Timestamp.valueOf(stockMovement.getMovementDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving stock movement", e);
        }
    }

    @Override
    public List<StockMovement> getStockMovementsByIngredientId(int ingredientId) {
        String query = "SELECT * FROM Stock_Movement WHERE ingredient_id = ? ORDER BY movement_date";
        List<StockMovement> stockMovements = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ingredientId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                StockMovement stockMovement = new StockMovement(
                        resultSet.getInt("movement_id"),
                        resultSet.getInt("ingredient_id"),
                        MovementType.valueOf(resultSet.getString("movement_type")),
                        resultSet.getDouble("quantity"),
                        Unit.valueOf(resultSet.getString("unit")),
                        resultSet.getTimestamp("movement_date").toLocalDateTime()
                );
                stockMovements.add(stockMovement);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving stock movements", e);
        }

        return stockMovements;
    }
}