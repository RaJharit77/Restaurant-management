package com.restaurant.dao;

import com.restaurant.entities.Dish;
import com.restaurant.entities.Ingredient;

import java.util.List;

public interface DishDAO {
    List<Dish> getAll();

    Dish findById(int id);

    List<Dish> saveAll(List<Dish> dishes);

    void deleteDish(int id);

    List<Dish> filterDish(String name, double unitPrice, List<Ingredient> dishIngredient);
}