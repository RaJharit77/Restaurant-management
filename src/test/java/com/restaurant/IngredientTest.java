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
    void testCreateAndFindById() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Tomate");
        ingredient.setUnitPrice(500);
        ingredient.setUnit(Unit.G);
        ingredient.setUpdateDateTime(LocalDateTime.now());

        ingredientDAO.createIngredient(ingredient);

        Ingredient foundIngredient = ingredientDAO.findById(ingredient.getId());
        assertNotNull(foundIngredient, "L'ingrédient n'a pas été trouvé");
        assertEquals("Tomate", foundIngredient.getName(), "Le nom de l'ingrédient ne correspond pas");
    }

    @Test
    void testUpdateIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Oignon");
        ingredient.setUnitPrice(300);
        ingredient.setUnit(Unit.G);
        ingredient.setUpdateDateTime(LocalDateTime.now());

        ingredientDAO.createIngredient(ingredient);

        ingredient.setUnitPrice(400);
        ingredientDAO.updateIngredient(ingredient);

        Ingredient updatedIngredient = ingredientDAO.findById(ingredient.getId());
        assertNotNull(updatedIngredient, "L'ingrédient n'a pas été trouvé");
        assertEquals(400, updatedIngredient.getUnitPrice(), "Le prix de l'ingrédient n'a pas été mis à jour");
    }

    @Test
    void testDeleteIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Fromage");
        ingredient.setUnitPrice(1000);
        ingredient.setUnit(Unit.G);
        ingredient.setUpdateDateTime(LocalDateTime.now());

        ingredientDAO.createIngredient(ingredient);

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
        ingredientDAO.createIngredient(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Oignon");
        ingredient2.setUnitPrice(300);
        ingredient2.setUnit(Unit.G);
        ingredient2.setUpdateDateTime(LocalDateTime.now());
        ingredientDAO.createIngredient(ingredient2);

        List<Ingredient> ingredients = ingredientDAO.filterIngredients("o", Unit.G, 100.0, 1000.0, 1, 10);
        assertFalse(ingredients.isEmpty(), "Aucun ingrédient trouvé");
        assertTrue(ingredients.stream().anyMatch(i -> i.getName().contains("Tomate")), "Tomate non trouvé");
        assertTrue(ingredients.stream().anyMatch(i -> i.getName().contains("Oignon")), "Oignon non trouvé");
    }
}