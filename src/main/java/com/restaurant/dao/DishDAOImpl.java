package com.restaurant.dao;

import com.restaurant.entities.Dish;
import com.restaurant.entities.Ingredient;
import com.restaurant.db.DataSource;
import com.restaurant.entities.Unit;

import java.sql.*;
import java.util.*;

public class DishDAOImpl implements DishDAO {
    private DataSource dataSource;

    public DishDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createDish(Dish dish) {
        String query = "INSERT INTO Dish (name, unit_price) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dish.getName());
            statement.setDouble(2, dish.getUnitPrice());
            statement.executeUpdate();

            // Récupérer l'ID généré pour le plat
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                dish.setId(generatedKeys.getInt(1));
            }

            // Insérer les relations dans Dish_Ingredient
            if (dish.getIngredients() != null) {
                for (Ingredient ingredient : dish.getIngredients()) {
                    String dishIngredientQuery = "INSERT INTO Dish_Ingredient (dish_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?::unit_type)";
                    try (PreparedStatement dishIngredientStatement = connection.prepareStatement(dishIngredientQuery)) {
                        dishIngredientStatement.setInt(1, dish.getId());
                        dishIngredientStatement.setInt(2, ingredient.getId());
                        dishIngredientStatement.setDouble(3, ingredient.getRequiredQuantity());
                        dishIngredientStatement.setString(4, ingredient.getUnit().name());
                        dishIngredientStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du plat", e);
        }
    }

    @Override
    public Dish findById(int id) {
        String query = "SELECT * FROM Dish WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("id"));
                dish.setName(resultSet.getString("name"));
                dish.setUnitPrice(resultSet.getDouble("unit_price"));
                dish.setIngredients(getIngredientsForDish(id));
                return dish;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du plat", e);
        }
        return null;
    }

    @Override
    public void updateDish(Dish dish) {
        String query = "UPDATE Dish SET name = ?, unit_price = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dish.getName());
            statement.setDouble(2, dish.getUnitPrice());
            statement.setInt(3, dish.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du plat", e);
        }
    }

    @Override
    public void deleteDish(int id) {
        String query = "DELETE FROM Dish WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du plat", e);
        }
    }

    private List<Ingredient> getIngredientsForDish(int dishId) {
        String query = "SELECT i.id, i.name, i.unit_price, i.unit, i.update_datetime, di.quantity " +
                "FROM Ingredient i " +
                "JOIN Dish_Ingredient di ON i.id = di.ingredient_id " +
                "WHERE di.dish_id = ?";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, dishId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("unit_price"),
                        Unit.valueOf(resultSet.getString("unit")),
                        resultSet.getTimestamp("update_datetime").toLocalDateTime(),
                        resultSet.getDouble("quantity")
                );
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients", e);
        }

        return ingredients;
    }
}