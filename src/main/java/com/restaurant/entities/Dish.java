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

    public double getIngredientCost() {
        return ingredients.stream()
                .mapToDouble(di -> di.getRequiredQuantity() * di.getUnitPrice())
                .sum();
    }
}
