package com.restaurant.dao;

import com.restaurant.entities.Ingredient;
import com.restaurant.entities.PriceHistory;
import com.restaurant.entities.StockMovement;
import com.restaurant.entities.Unit;
import com.restaurant.db.DataSource;

import java.sql.*;
import java.util.*;

public class IngredientDAOImpl implements IngredientDAO {
    private final DataSource dataSource;

    public IngredientDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Ingredient> getAll() {
        String query = "SELECT * FROM Ingredient";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient(
                        resultSet.getInt("ingredient_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("unit_price"),
                        Unit.valueOf(resultSet.getString("unit")),
                        resultSet.getTimestamp("update_datetime").toLocalDateTime(),
                        0
                );
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all ingredients", e);
        }

        return ingredients;
    }

    @Override
    public Ingredient findById(int id) {
        String query = "SELECT * FROM Ingredient WHERE ingredient_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("ingredient_id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setUnitPrice(resultSet.getDouble("unit_price"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredient.setUpdateDateTime(resultSet.getTimestamp("update_datetime").toLocalDateTime());
                return ingredient;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving ingredient", e);
        }
        return null;
    }

    @Override
    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        String insertQuery = "INSERT INTO Ingredient (name, unit_price, unit, update_datetime) VALUES (?, ?, ?::unit_type, ?)";
        String updateQuery = "UPDATE Ingredient SET name = ?, unit_price = ?, unit = ?::unit_type, update_datetime = ? WHERE ingredient_id = ?";  // Changé ici
        String priceHistoryQuery = "INSERT INTO Price_History (ingredient_id, price, date) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                 PreparedStatement priceHistoryStatement = connection.prepareStatement(priceHistoryQuery)) {

                for (Ingredient ingredient : ingredients) {
                    if (ingredient.getId() == 0) {
                        insertStatement.setString(1, ingredient.getName());
                        insertStatement.setDouble(2, ingredient.getUnitPrice());
                        insertStatement.setString(3, ingredient.getUnit().name());
                        insertStatement.setTimestamp(4, java.sql.Timestamp.valueOf(ingredient.getUpdateDateTime()));
                        insertStatement.executeUpdate();

                        ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            ingredient.setId(generatedKeys.getInt(1));
                        }
                    } else {
                        updateStatement.setString(1, ingredient.getName());
                        updateStatement.setDouble(2, ingredient.getUnitPrice());
                        updateStatement.setString(3, ingredient.getUnit().name());
                        updateStatement.setTimestamp(4, java.sql.Timestamp.valueOf(ingredient.getUpdateDateTime()));
                        updateStatement.setInt(5, ingredient.getId());
                        updateStatement.executeUpdate();
                    }

                    for (PriceHistory priceHistory : ingredient.getPriceHistory()) {
                        priceHistoryStatement.setInt(1, ingredient.getId());
                        priceHistoryStatement.setDouble(2, priceHistory.getPrice());
                        priceHistoryStatement.setTimestamp(3, java.sql.Timestamp.valueOf(priceHistory.getDate()));
                        priceHistoryStatement.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error saving ingredients", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error managing database connection", e);
        }

        return ingredients;
    }

    private List<PriceHistory> getPriceHistoryForIngredient(int ingredientId) {
        String query = "SELECT price, date FROM Price_History WHERE ingredient_id = ? ORDER BY date DESC";
        List<PriceHistory> priceHistory = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ingredientId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                priceHistory.add(new PriceHistory(
                        resultSet.getDouble("price"),
                        resultSet.getTimestamp("date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving price history", e);
        }

        return priceHistory;
    }

    @Override
    public void deleteIngredient(int id) {
        String query = "DELETE FROM Ingredient WHERE ingredient_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'ingrédient", e);
        }
    }

    @Override
    public List<Ingredient> filterIngredients(String name, Unit unit, Double minPrice, Double maxPrice, int page, int pageSize) {
        String query = "SELECT * FROM Ingredient WHERE 1=1";
        List<Object> parameters = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            query += " AND name ILIKE ?";
            parameters.add("%" + name + "%");
        }
        if (unit != null) {
            query += " AND unit = ?::unit_type";
            parameters.add(unit.name());
        }
        if (minPrice != null) {
            query += " AND unit_price >= ?";
            parameters.add(minPrice);
        }
        if (maxPrice != null) {
            query += " AND unit_price <= ?";
            parameters.add(maxPrice);
        }

        query += " LIMIT ? OFFSET ?";
        parameters.add(pageSize);
        parameters.add((page - 1) * pageSize);

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient(
                        resultSet.getInt("ingredient_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("unit_price"),
                        Unit.valueOf(resultSet.getString("unit")),
                        resultSet.getTimestamp("update_datetime").toLocalDateTime(),
                        0

                );
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error filtering ingredients", e);
        }

        return ingredients;
    }

    public void addStockMovement(StockMovement movement) {
        String query = "INSERT INTO Stock_Movement (ingredient_id, movement_type, quantity, unit, movement_date) VALUES (?, ?::movement_type, ?, ?::unit_type, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, movement.getIngredientId());
            statement.setString(2, movement.getMovementType().name());
            statement.setDouble(3, movement.getQuantity());
            statement.setString(4, movement.getUnit().name());
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(movement.getMovementDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding stock movement", e);
        }
    }
}