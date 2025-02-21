package com.restaurant.dao;

import com.restaurant.entities.Ingredient;
import com.restaurant.entities.Unit;

import java.util.List;

public interface IngredientDAO {
    void createIngredient(Ingredient ingredient);

    Ingredient findById(int id);

    void updateIngredient(Ingredient ingredient);

    void deleteIngredient(int id);

    List<Ingredient> filterIngredients(String name, Unit unit, Double minPrice, Double maxPrice, int page, int pageSize);
}
