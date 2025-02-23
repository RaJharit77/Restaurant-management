package com.restaurant.entities;

import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Dish {
    private int id;
    private String name;
    private double unitPrice;
    private List<Ingredient> ingredients;

    public Dish() {
    }

    public Dish(int id, String name, double unitPrice, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.ingredients = ingredients;
    }

    public double getIngredientCost() {
        return ingredients.stream()
                .mapToDouble(di -> di.getRequiredQuantity() * di.getUnitPrice())
                .sum();
    }
}
