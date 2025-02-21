package test;

import dao.IngredientDAO;
import dao.IngredientDAOImpl;
import entities.Ingredient;
import entities.Unit;
import db.DataSource;
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
        assertNotNull(foundIngredient);
        assertEquals("Tomate", foundIngredient.getName());
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
        assertEquals(400, updatedIngredient.getUnitPrice());
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
        assertNull(deletedIngredient);
    }

    @Test
    void testFilterIngredients() {
        List<Ingredient> ingredients = ingredientDAO.filterIngredients("o", Unit.G, 100.0, 1000.0, 1, 10);
        assertFalse(ingredients.isEmpty());
    }
}