package com.restaurant.dao;

import com.restaurant.entities.Ingredient;
import com.restaurant.entities.Unit;
import com.restaurant.db.DataSource;

import java.sql.*;
import java.util.*;

public class IngredientDAOImpl implements IngredientDAO {
    private DataSource dataSource;

    public IngredientDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createIngredient(Ingredient ingredient) {
        String query = "INSERT INTO Ingredient (name, unit_price, unit, update_datetime) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ingredient.getName());
            statement.setDouble(2, ingredient.getUnitPrice());
            statement.setString(3, ingredient.getUnit().name());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(ingredient.getUpdateDateTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'ingrédient", e);
        }
    }

    @Override
    public Ingredient findById(int id) {
        String query = "SELECT * FROM Ingredient WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setUnitPrice(resultSet.getDouble("unit_price"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredient.setUpdateDateTime(resultSet.getTimestamp("update_datetime").toLocalDateTime());
                return ingredient;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'ingrédient", e);
        }
        return null;
    }

    @Override
    public void updateIngredient(Ingredient ingredient) {
        String query = "UPDATE Ingredient SET name = ?, unit_price = ?, unit = ?, update_datetime = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ingredient.getName());
            statement.setDouble(2, ingredient.getUnitPrice());
            statement.setString(3, ingredient.getUnit().name());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(ingredient.getUpdateDateTime()));
            statement.setInt(5, ingredient.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'ingrédient", e);
        }
    }

    @Override
    public void deleteIngredient(int id) {
        String query = "DELETE FROM Ingredient WHERE id = ?";
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
            query += " AND unit = ?";
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

        query += " ORDER BY name ASC LIMIT ? OFFSET ?";
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
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setUnitPrice(resultSet.getDouble("unit_price"));
                ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                ingredient.setUpdateDateTime(resultSet.getTimestamp("update_datetime").toLocalDateTime());
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du filtrage des ingrédients", e);
        }

        return ingredients;
    }
}