package entities;

import java.util.List;
import java.util.Objects;

public class Dish {
    private int id;
    private String name;
    private double unitPrice;
    private List<Ingredient> ingredients;

    public Dish() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public double getIngredientCost() {
        return ingredients.stream()
                .mapToDouble(ingredient -> ingredient.getUnitPrice() * ingredient.getRequiredQuantity())
                .sum();
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", ingredients=" + ingredients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && Double.compare(unitPrice, dish.unitPrice) == 0 && Objects.equals(name, dish.name) && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitPrice, ingredients);
    }
}