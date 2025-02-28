package com.restaurant.dao;

import com.restaurant.entities.Ingredient;
import com.restaurant.entities.StockMovement;
import com.restaurant.entities.Unit;

import java.util.List;

public interface IngredientDAO {
    List<Ingredient> getAll();

    Ingredient findById(int id);

    List<Ingredient> saveAll(List<Ingredient> ingredients);

    void deleteIngredient(int id);

    List<Ingredient> filterIngredients(String name, Unit unit, Double minPrice, Double maxPrice, int page, int pageSize);

    void addStockMovement(StockMovement movement);
}