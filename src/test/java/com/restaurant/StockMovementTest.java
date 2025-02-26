package com.restaurant;

import com.restaurant.dao.StockMovementImpl;
import com.restaurant.entities.StockMovement;
import com.restaurant.entities.MovementType;
import com.restaurant.entities.Unit;
import com.restaurant.db.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockMovementTest {
    private static StockMovementImpl stockMovementImpl;

    @BeforeAll
    static void setUp() {
        DataSource dataSource = new DataSource();
        stockMovementImpl = new StockMovementImpl(dataSource);
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
}