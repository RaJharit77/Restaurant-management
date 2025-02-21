package com.restaurant;

import com.restaurant.dao.DishDAO;
import com.restaurant.dao.DishDAOImpl;
import com.restaurant.entities.Dish;
import com.restaurant.db.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DishTest {
    private static DishDAO dishDAO;

    @BeforeAll
    static void setUp() {
        DataSource dataSource = new DataSource();
        dishDAO = new DishDAOImpl(dataSource);
    }

    @Test
    void testCreateAndFindById() {
        Dish dish = new Dish();
        dish.setName("Pizza");
        dish.setUnitPrice(12000);

        dishDAO.createDish(dish);

        Dish foundDish = dishDAO.findById(dish.getId());
        assertNotNull(foundDish);
        assertEquals("Pizza", foundDish.getName());
    }

    @Test
    void testUpdateDish() {
        Dish dish = new Dish();
        dish.setName("Burger");
        dish.setUnitPrice(8000);

        dishDAO.createDish(dish);

        dish.setUnitPrice(9000);
        dishDAO.updateDish(dish);

        Dish updatedDish = dishDAO.findById(dish.getId());
        assertEquals(9000, updatedDish.getUnitPrice());
    }

    @Test
    void testDeleteDish() {
        Dish dish = new Dish();
        dish.setName("Salade");
        dish.setUnitPrice(5000);

        dishDAO.createDish(dish);
        dishDAO.deleteDish(dish.getId());

        Dish deletedDish = dishDAO.findById(dish.getId());
        assertNull(deletedDish);
    }
}