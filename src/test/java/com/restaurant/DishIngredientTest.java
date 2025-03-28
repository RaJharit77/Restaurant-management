package com.restaurant;

import com.restaurant.dao.*;
import com.restaurant.db.DataBaseSource;
import com.restaurant.entities.Dish;
import com.restaurant.entities.Ingredient;
import com.restaurant.entities.Unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.List;

public class DishIngredientTest {
    private DishDAO dishDAO;
    private IngredientDAO ingredientDAO;
    private DataBaseSource dataBaseSource;

    @BeforeEach
    void setUp() {
        dataBaseSource = new DataBaseSource();
        dishDAO = new DishDAOImpl(dataBaseSource);
        ingredientDAO = new IngredientDAOImpl(dataBaseSource);
    }

    @Test
    void testCreateAndFindDish() {
        Ingredient sausage = new Ingredient(1, "Saucisse", 20, Unit.G, LocalDateTime.now(), 100);
        Ingredient oil = new Ingredient(2, "Huile", 10000, Unit.L, LocalDateTime.now(), 0.15);
        ingredientDAO.saveAll(List.of(sausage, oil));

        Dish hotDog = new Dish();
        hotDog.setName("Hot Dog");
        hotDog.setUnitPrice(15000);
        hotDog.setIngredients(List.of(sausage, oil));
        dishDAO.saveAll(List.of(hotDog));

        Dish retrievedDish = dishDAO.findById(hotDog.getId());
        assertNotNull(retrievedDish);
        assertEquals("Hot Dog", retrievedDish.getName());
        assertEquals(15000, retrievedDish.getUnitPrice());
        assertEquals(2, retrievedDish.getIngredients().size());

        double expectedCost = (100 * 20) + (0.15 * 10000);
        assertEquals(expectedCost, retrievedDish.getIngredientCost());
    }

    @Test
    void testFilterIngredients() {
        Ingredient sausage = new Ingredient(1, "Saucisse", 20, Unit.G, LocalDateTime.now(), 100);
        Ingredient oil = new Ingredient(2, "Huile", 10000, Unit.L, LocalDateTime.now(), 0.15);
        ingredientDAO.saveAll(List.of(sausage, oil));

        List<Ingredient> filteredIngredients = ingredientDAO.filterIngredients("u", Unit.G, 10.0, 1000.0, 1, 1);

        assertEquals(1, filteredIngredients.size(), "Le nombre d'ingrédients filtrés est incorrect");
        assertEquals("Saucisse", filteredIngredients.getFirst().getName(), "L'ingrédient filtré est incorrect");
    }

    @Test
    void testFilterDishByName() {
        try (Connection connection = dataBaseSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM Dish_Ingredient");
            statement.execute("DELETE FROM Dish");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du nettoyage de la base de données", e);
        }

        Dish burger = new Dish();
        burger.setName("Cheeseburger");
        burger.setUnitPrice(12000);
        dishDAO.saveAll(List.of(burger));

        List<Dish> results = dishDAO.filterDish("Cheeseburger", 0, null);
        assertFalse(results.isEmpty(), "Results should not be empty");
        assertEquals(1, results.size(), "Only one Cheeseburger should be found");
        assertEquals("Cheeseburger", results.getFirst().getName());
    }

    @Test
    void testFilterDishByPrice() {
        Dish pizza = new Dish();
        pizza.setName("Pizza");
        pizza.setUnitPrice(8000);
        dishDAO.saveAll(List.of(pizza));

        List<Dish> results = dishDAO.filterDish(null, 10000, null);
        assertFalse(results.isEmpty(), "Results should not be empty");
        assertTrue(results.stream().anyMatch(d -> d.getName().equals("Pizza")), "Pizza should be in results");
    }

    @Test
    void testFilterDishByIngredients() {
        Ingredient tomato = new Ingredient(3, "Tomato", 500, Unit.G, LocalDateTime.now(), 200);
        Ingredient cheese = new Ingredient(4, "Cheese", 700, Unit.G, LocalDateTime.now(), 150);
        ingredientDAO.saveAll(List.of(tomato, cheese));

        Dish salad = new Dish();
        salad.setName("Salade");
        salad.setUnitPrice(5000);
        salad.setIngredients(List.of(tomato, cheese));
        dishDAO.saveAll(List.of(salad));

        List<Dish> results = dishDAO.filterDish(null, 0, List.of(tomato, cheese));
        assertFalse(results.isEmpty(), "Results should not be empty");
        assertTrue(results.stream().anyMatch(d -> d.getName().equals("Salade")), "Salade should be in results");
    }

    @Test
    void testFilterDishByAllCriteria() {
        Ingredient tomato = new Ingredient(3, "Tomato", 500, Unit.G, LocalDateTime.now(), 200);
        Ingredient cheese = new Ingredient(4, "Cheese", 700, Unit.G, LocalDateTime.now(), 150);
        ingredientDAO.saveAll(List.of(tomato, cheese));

        Dish hamburger = new Dish();
        hamburger.setName("Hamburger");
        hamburger.setUnitPrice(5000);
        hamburger.setIngredients(List.of(tomato, cheese));
        dishDAO.saveAll(List.of(hamburger));

        List<Dish> results = dishDAO.filterDish("Hamburger", 5000, List.of(tomato, cheese));

        assertFalse(results.isEmpty(), "Results should not be empty");
        assertTrue(results.stream().anyMatch(d -> d.getName().equals("Hamburger")), "Hamburger should be in results");
    }
}