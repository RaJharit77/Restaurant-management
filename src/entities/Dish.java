package entities;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Dish {
    private int id;
    private String name;
    private double unitPrice;
    private List<Ingredient> ingredients;
    private Map<Ingredient, Double> requiredQuantities;

    public Dish(int id, String name, double unitPrice, List<Ingredient> ingredients, Map<Ingredient, Double> requiredQuantities) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.ingredients = ingredients;
        this.requiredQuantities = requiredQuantities;
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

    public Map<Ingredient, Double> getRequiredQuantities() {
        return requiredQuantities;
    }

    public void setRequiredQuantities(Map<Ingredient, Double> requiredQuantities) {
        this.requiredQuantities = requiredQuantities;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", ingredients=" + ingredients +
                ", requiredQuantities=" + requiredQuantities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && Double.compare(unitPrice, dish.unitPrice) == 0 && Objects.equals(name, dish.name) && Objects.equals(ingredients, dish.ingredients) && Objects.equals(requiredQuantities, dish.requiredQuantities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitPrice, ingredients, requiredQuantities);
    }
}
