package com.restaurant;

import com.restaurant.dao.DishDAO;
import com.restaurant.dao.DishDAOImpl;
import com.restaurant.dao.IngredientDAO;
import com.restaurant.dao.IngredientDAOImpl;
import com.restaurant.db.DataSource;
import com.restaurant.entities.Dish;
import com.restaurant.entities.Ingredient;
import com.restaurant.entities.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DishIngredientTest {
    private DishDAO dishDAO;
    private IngredientDAO ingredientDAO;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new DataSource();
        dishDAO = new DishDAOImpl(dataSource);
        ingredientDAO = new IngredientDAOImpl(dataSource);
    }

    @Test
    void testCreateAndFindDish() {
        Ingredient sausage = new Ingredient(1, "Saucisse", 20, Unit.G, LocalDateTime.now(), 100);
        Ingredient oil = new Ingredient(2, "Huile", 10000, Unit.L, LocalDateTime.now(), 0.15);
        ingredientDAO.createIngredient(sausage);
        ingredientDAO.createIngredient(oil);

        Dish hotDog = new Dish();
        hotDog.setName("Hot Dog");
        hotDog.setUnitPrice(15000);
        hotDog.setIngredients(List.of(sausage, oil));
        dishDAO.createDish(hotDog);

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
        ingredientDAO.createIngredient(sausage);
        ingredientDAO.createIngredient(oil);

        List<Ingredient> filteredIngredients = ingredientDAO.filterIngredients("u", Unit.G, 10.0, 1000.0, 1, 10);
        assertEquals(1, filteredIngredients.size());
        assertEquals("Saucisse", filteredIngredients.getFirst().getName());
    }
}