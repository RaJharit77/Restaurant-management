package com.restaurant;

import com.restaurant.db.DataSource;
import com.restaurant.dao.StockMovementImpl;
import com.restaurant.dao.IngredientDAO;
import com.restaurant.dao.IngredientDAOImpl;
import com.restaurant.entities.StockMovement;
import com.restaurant.entities.Ingredient;
import com.restaurant.entities.MovementType;
import com.restaurant.entities.Unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockMovementTest {
    private StockMovementImpl stockMovementImpl;
    private IngredientDAO ingredientDAO;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new DataSource();
        stockMovementImpl = new StockMovementImpl(dataSource);
        ingredientDAO = new IngredientDAOImpl(dataSource);
        resetDatabase();
    }

    private void resetDatabase() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM Stock_Movement");
        } catch (SQLException e) {
            throw new RuntimeException("Error resetting database", e);
        }
    }

    @Test
    void testSaveStockMovement() {
        StockMovement stockMovement = new StockMovement(0, 1, MovementType.ENTRY, 100, Unit.U, LocalDateTime.of(2025, 2, 1, 8, 0));
        stockMovementImpl.saveStockMovement(stockMovement);

        List<StockMovement> stockMovements = stockMovementImpl.getStockMovementsByIngredientId(1);
        assertFalse(stockMovements.isEmpty());
        assertEquals(1, stockMovements.size());
        assertEquals(100, stockMovements.getFirst().getQuantity());
    }

    @Test
    void testGetStockMovementsByIngredientId() {
        StockMovement stockMovement1 = new StockMovement(0, 1, MovementType.ENTRY, 100, Unit.U, LocalDateTime.of(2025, 2, 1, 8, 0));
        StockMovement stockMovement2 = new StockMovement(0, 1, MovementType.EXIT, 10, Unit.U, LocalDateTime.of(2025, 2, 2, 10, 0));
        stockMovementImpl.saveStockMovement(stockMovement1);
        stockMovementImpl.saveStockMovement(stockMovement2);

        List<StockMovement> stockMovements = stockMovementImpl.getStockMovementsByIngredientId(1);
        assertFalse(stockMovements.isEmpty());
        assertEquals(2, stockMovements.size());
        assertEquals(100, stockMovements.get(0).getQuantity());
        assertEquals(10, stockMovements.get(1).getQuantity());
    }

    @Test
    void testStockMovementsForSelAndRiz() {
        Ingredient sel = new Ingredient(0, "Sel", 2.5, Unit.G, LocalDateTime.now(), 0);
        Ingredient riz = new Ingredient(0, "Riz", 3.5, Unit.G, LocalDateTime.now(), 0);

        List<Ingredient> savedIngredients = ingredientDAO.saveAll(List.of(sel, riz));
        assertNotNull(savedIngredients);
        assertEquals(2, savedIngredients.size());

        int selId = savedIngredients.get(0).getId();
        int rizId = savedIngredients.get(1).getId();

        StockMovement selEntry1 = new StockMovement(0, selId, MovementType.ENTRY, 500, Unit.G, LocalDateTime.of(2025, 2, 1, 8, 0));
        StockMovement selExit1 = new StockMovement(0, selId, MovementType.EXIT, 100, Unit.G, LocalDateTime.of(2025, 2, 2, 10, 0));
        stockMovementImpl.saveStockMovement(selEntry1);
        stockMovementImpl.saveStockMovement(selExit1);

        StockMovement rizEntry1 = new StockMovement(0, rizId, MovementType.ENTRY, 1000, Unit.G, LocalDateTime.of(2025, 2, 1, 8, 0));
        StockMovement rizExit1 = new StockMovement(0, rizId, MovementType.EXIT, 200, Unit.G, LocalDateTime.of(2025, 2, 2, 10, 0));
        stockMovementImpl.saveStockMovement(rizEntry1);
        stockMovementImpl.saveStockMovement(rizExit1);

        List<StockMovement> selMovements = stockMovementImpl.getStockMovementsByIngredientId(selId);
        assertFalse(selMovements.isEmpty());
        assertEquals(2, selMovements.size());
        assertEquals(500, selMovements.get(0).getQuantity());
        assertEquals(100, selMovements.get(1).getQuantity());

        List<StockMovement> rizMovements = stockMovementImpl.getStockMovementsByIngredientId(rizId);
        assertFalse(rizMovements.isEmpty());
        assertEquals(2, rizMovements.size());
        assertEquals(1000, rizMovements.get(0).getQuantity());
        assertEquals(200, rizMovements.get(1).getQuantity());
    }
}