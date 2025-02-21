package dao;

import entities.Dish;

public interface DishDAO {
    void createDish(Dish dish);

    Dish findById(int id);

    void updateDish(Dish dish);

    void deleteDish(int id);
}
