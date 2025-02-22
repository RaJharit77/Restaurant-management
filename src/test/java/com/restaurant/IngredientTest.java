package com.restaurant;

import com.restaurant.dao.IngredientDAO;
import com.restaurant.dao.IngredientDAOImpl;
import com.restaurant.entities.Ingredient;
import com.restaurant.entities.Unit;
import com.restaurant.db.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {
    private static IngredientDAO ingredientDAO;

    @BeforeAll
    static void setUp() {
        DataSource dataSource = new DataSource();
        ingredientDAO = new IngredientDAOImpl(dataSource);
    }

    @Test
    void testSaveAllAndGetAllIngredients() {
        Ingredient ingredient1 = new Ingredient(0, "Tomate", 500, Unit.G, LocalDateTime.now(), 0);
        Ingredient ingredient2 = new Ingredient(0, "Oignon", 300, Unit.G, LocalDateTime.now(), 0);

        List<Ingredient> savedIngredients = ingredientDAO.saveAll(List.of(ingredient1, ingredient2));
        assertNotNull(savedIngredients);
        assertEquals(2, savedIngredients.size());

        List<Ingredient> allIngredients = ingredientDAO.getAll();
        assertFalse(allIngredients.isEmpty());
        assertTrue(allIngredients.stream().anyMatch(i -> i.getName().equals("Tomate")));
        assertTrue(allIngredients.stream().anyMatch(i -> i.getName().equals("Oignon")));
    }

    @Test
    void testDeleteIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Fromage");
        ingredient.setUnitPrice(1000);
        ingredient.setUnit(Unit.G);
        ingredient.setUpdateDateTime(LocalDateTime.now());

        ingredientDAO.saveAll(List.of(ingredient));

        ingredientDAO.deleteIngredient(ingredient.getId());

        Ingredient deletedIngredient = ingredientDAO.findById(ingredient.getId());
        assertNull(deletedIngredient, "L'ingrédient n'a pas été supprimé");
    }

    @Test
    void testFilterIngredients() {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Tomate");
        ingredient1.setUnitPrice(500);
        ingredient1.setUnit(Unit.G);
        ingredient1.setUpdateDateTime(LocalDateTime.now());
        ingredientDAO.saveAll(List.of(ingredient1));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Oignon");
        ingredient2.setUnitPrice(300);
        ingredient2.setUnit(Unit.G);
        ingredient2.setUpdateDateTime(LocalDateTime.now());
        ingredientDAO.saveAll(List.of(ingredient2));

        List<Ingredient> ingredients = ingredientDAO.filterIngredients("o", Unit.G, 100.0, 1000.0, 1, 10);
        assertFalse(ingredients.isEmpty(), "Aucun ingrédient trouvé");
        assertTrue(ingredients.stream().anyMatch(i -> i.getName().contains("Tomate")), "Tomate non trouvé");
        assertTrue(ingredients.stream().anyMatch(i -> i.getName().contains("Oignon")), "Oignon non trouvé");
    }
}