package com.restaurant.services;

import com.restaurant.dao.DishDAO;
import com.restaurant.entities.Dish;
import com.restaurant.entities.Ingredient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServices {
    private final DishDAO dishDAO;

    public DishServices(DishDAO dishDAO) {
        this.dishDAO = dishDAO;
    }

    @Transactional(readOnly = true)
    public List<Dish> getAllDishes() {
        return dishDAO.getAll();
    }

    @Transactional(readOnly = true)
    public Dish getDishById(int id) {
        return dishDAO.findById(id);
    }

    @Transactional
    public List<Dish> saveAllDishes(List<Dish> dishes) {
        return dishDAO.saveAll(dishes);
    }

    @Transactional
    public void deleteDish(int id) {
        dishDAO.deleteDish(id);
    }

    @Transactional(readOnly = true)
    public List<Dish> filterDishes(String name, double unitPrice, List<Ingredient> ingredients) {
        return dishDAO.filterDish(name, unitPrice, ingredients);
    }
}