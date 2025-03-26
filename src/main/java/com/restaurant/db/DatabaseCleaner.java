package com.restaurant.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DatabaseCleaner {
    private static final List<String> TABLES_TO_CLEAN = Arrays.asList(
            "Dish_Order_Status",
            "Order_Status",
            "Dish_Order",
            "\"Order\"",
            "Stock_Movement",
            "Price_History",
            "Dish_Ingredient",
            "Dish",
            "Ingredient"
    );

    private final DataSource dataSource;

    public DatabaseCleaner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void cleanAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("SET CONSTRAINTS ALL DEFERRED");

            for (String table : TABLES_TO_CLEAN) {
                try {
                    statement.execute("DELETE FROM " + table);
                } catch (SQLException e) {
                    System.err.println("Erreur lors du nettoyage de la table " + table + ": " + e.getMessage());
                }
            }

            statement.execute("SET CONSTRAINTS ALL IMMEDIATE");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du nettoyage de la base de données", e);
        }
    }

    public void cleanSpecificTables(String... tables) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("SET CONSTRAINTS ALL DEFERRED");

            for (String table : tables) {
                try {
                    statement.execute("DELETE FROM " + table);
                } catch (SQLException e) {
                    System.err.println("Erreur lors du nettoyage de la table " + table + ": " + e.getMessage());
                }
            }

            statement.execute("SET CONSTRAINTS ALL IMMEDIATE");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du nettoyage des tables spécifiées", e);
        }
    }
}