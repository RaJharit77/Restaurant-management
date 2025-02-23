package com.restaurant;

import com.restaurant.dao.DishDAO;
import com.restaurant.dao.DishDAOImpl;
import com.restaurant.dao.IngredientDAO;
import com.restaurant.dao.IngredientDAOImpl;
import com.restaurant.entities.Dish;
import com.restaurant.db.DataSource;
import com.restaurant.entities.Ingredient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.restaurant.entities.Unit;
import static org.junit.jupiter.api.Assertions.*;

class DishTest {
    private static DishDAO dishDAO;
    private static IngredientDAO ingredientDAO;

    @BeforeAll
    static void setUp() {
        DataSource dataSource = new DataSource();
        dishDAO = new DishDAOImpl(dataSource);
        ingredientDAO = new IngredientDAOImpl(dataSource);
    }

    @Test
    void testSaveAllAndGetAllDishes() {
        Ingredient ingredient1 = new Ingredient(1, "Tomate", 500, Unit.G, LocalDateTime.now(), 0);
        Ingredient ingredient2 = new Ingredient(2, "Oignon", 300, Unit.G, LocalDateTime.now(), 0);

        Dish dish1 = new Dish();
        dish1.setName("Pizza");
        dish1.setUnitPrice(12000);
        dish1.setIngredients(List.of(ingredient1, ingredient2));

        Dish dish2 = new Dish();
        dish2.setName("Burger");
        dish2.setUnitPrice(8000);
        dish2.setIngredients(List.of(ingredient1));

        List<Dish> savedDishes = dishDAO.saveAll(List.of(dish1, dish2));
        assertNotNull(savedDishes);
        assertEquals(2, savedDishes.size());

        List<Dish> allDishes = dishDAO.getAll();
        assertFalse(allDishes.isEmpty());
        assertTrue(allDishes.stream().anyMatch(d -> d.getName().equals("Pizza")));
        assertTrue(allDishes.stream().anyMatch(d -> d.getName().equals("Burger")));
    }

    @Test
    void testFilterDish() {
        Ingredient ingredient1 = new Ingredient(1, "Tomate", 500, Unit.G, LocalDateTime.now(), 0);
        Ingredient ingredient2 = new Ingredient(2, "Oignon", 300, Unit.G, LocalDateTime.now(), 0);

        Dish dish1 = new Dish();
        dish1.setName("Pizza");
        dish1.setUnitPrice(12000);
        dish1.setIngredients(List.of(ingredient1, ingredient2));

        Dish dish2 = new Dish();
        dish2.setName("Burger");
        dish2.setUnitPrice(8000);
        dish2.setIngredients(List.of(ingredient1));

        dishDAO.saveAll(List.of(dish1, dish2));

        List<Dish> filteredDishes = dishDAO.filterDish("Pizza", 15000, List.of(ingredient1));

        assertNotNull(filteredDishes, "Filtered dishes should not be null");
        assertFalse(filteredDishes.isEmpty(), "Filtered dishes should not be empty");
        assertEquals(1, filteredDishes.size(), "Only one dish should be returned");
        assertEquals("Pizza", filteredDishes.get(0).getName(), "Dish name should match 'Pizza'");

        List<Dish> noResult = dishDAO.filterDish("Pizza", 15000, List.of(new Ingredient(99, "Inexistant", 0, Unit.G, LocalDateTime.now(), 0)));

        assertTrue(noResult.isEmpty(), "No dishes should be found for non-existing ingredient");
    }

    @Test
    void testDeleteDish() {
        Dish dish = new Dish();
        dish.setName("Salade");
        dish.setUnitPrice(5000);

        dishDAO.saveAll(List.of(dish));
        dishDAO.deleteDish(dish.getId());

        Dish deletedDish = dishDAO.findById(dish.getId());
        assertNull(deletedDish);
    }
}