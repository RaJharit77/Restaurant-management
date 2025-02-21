package com.restaurant;

import com.restaurant.dao.DishDAO;
import com.restaurant.dao.DishDAOImpl;
import com.restaurant.dao.IngredientDAO;
import com.restaurant.dao.IngredientDAOImpl;
import com.restaurant.db.DataSource;
import com.restaurant.entities.Dish;
import com.restaurant.entities.Ingredient;
import com.restaurant.entities.Unit;

import java.time.LocalDateTime;
import java.util.List;

public class RestaurantManagementApp {
    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        DishDAO dishDAO = new DishDAOImpl(dataSource);
        IngredientDAO ingredientDAO = new IngredientDAOImpl(dataSource);

        Ingredient sausage = new Ingredient(1, "Saucisse", 20, Unit.G, LocalDateTime.now(), 100);
        ingredientDAO.createIngredient(sausage);

        Dish hotDog = new Dish();
        hotDog.setName("Hot Dog");
        hotDog.setUnitPrice(15000);
        hotDog.setIngredients(List.of(sausage));
        dishDAO.createDish(hotDog);

        Dish retrievedDish = dishDAO.findById(hotDog.getId());

        if (retrievedDish != null) {
            System.out.println("Coût des ingrédients du plat: " + retrievedDish.getIngredientCost());
        } else {
            System.out.println("Le plat n'a pas été trouvé dans la base de données.");
        }
    }
}