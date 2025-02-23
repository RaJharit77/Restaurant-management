package com.restaurant;

import com.restaurant.dao.DishDAO;
import com.restaurant.dao.DishDAOImpl;
import com.restaurant.dao.IngredientDAO;
import com.restaurant.dao.IngredientDAOImpl;
import com.restaurant.entities.Dish;
import com.restaurant.db.DataSource;
import com.restaurant.entities.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import com.restaurant.entities.Unit;
import static org.junit.jupiter.api.Assertions.*;

class DishTest {
    private DishDAO dishDAO;
    private IngredientDAO ingredientDAO;

    @BeforeEach
    void setUp() {
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
        Ingredient tomato = new Ingredient(1, "Tomato", 500, Unit.G, LocalDateTime.now(), 200);
        Ingredient cheese = new Ingredient(2, "Cheese", 700, Unit.G, LocalDateTime.now(), 150);

        dishDAO.saveAll(List.of(new Dish(0, "Sandwich", 5000, List.of(tomato, cheese))));

        List<Dish> results = dishDAO.filterDish(null, 0, List.of(tomato, cheese));

        assertFalse(results.isEmpty(), "Results should not be empty");
        assertTrue(results.stream().anyMatch(d -> d.getName().equals("Sandwich")), "Sandwich should be in results");
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