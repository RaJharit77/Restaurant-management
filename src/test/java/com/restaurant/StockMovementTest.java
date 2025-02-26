package com.restaurant;

import com.restaurant.dao.StockMovementImpl;
import com.restaurant.entities.StockMovement;
import com.restaurant.entities.MovementType;
import com.restaurant.entities.Unit;
import com.restaurant.db.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockMovementTest {
    private StockMovementImpl stockMovementImpl;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new DataSource();
        stockMovementImpl = new StockMovementImpl(dataSource);
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
}