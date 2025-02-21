package dao;

import entities.Ingredient;
import entities.Unit;

import java.util.List;

public interface IngredientDAO {
    void createIngredient(Ingredient ingredient);

    Ingredient findById(int id);

    void updateIngredient(Ingredient ingredient);

    void deleteIngredient(int id);

    List<Ingredient> filterIngredients(String name, Unit unit, Double minPrice, Double maxPrice, int page, int pageSize);
}
