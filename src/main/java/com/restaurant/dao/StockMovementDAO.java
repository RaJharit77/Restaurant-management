package com.restaurant.dao;

import com.restaurant.entities.StockMovement;

import java.util.List;

public interface StockMovementDAO {
    void saveStockMovement(StockMovement stockMovement);

    List<StockMovement> getStockMovementsByIngredientId(int ingredientId);
}
