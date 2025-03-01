package com.restaurant.entities;

import java.time.LocalDateTime;
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
        return getIngredientCostAtDate(LocalDateTime.now());
    }

    public double getIngredientCostAtDate(LocalDateTime date) {
        return ingredients.stream()
                .mapToDouble(di -> di.getRequiredQuantity() * di.getPriceAtDate(date))
                .sum();
    }

    public double getGrossMargin() {
        return getGrossMarginAtDate(LocalDateTime.now());
    }

    public double getGrossMarginAtDate(LocalDateTime date) {
        return unitPrice - getIngredientCostAtDate(date);
    }

    public double getAvailableQuantity(LocalDateTime date) {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0;
        }

        double availableQuantity = Double.MAX_VALUE;

        for (Ingredient ingredient : ingredients) {
            double ingredientAvailableQuantity = ingredient.getAvailableQuantity(date);
            double requiredQuantity = ingredient.getRequiredQuantity();

            if (requiredQuantity <= 0) {
                continue;
            }

            double dishQuantityForIngredient = ingredientAvailableQuantity / requiredQuantity;

            if (dishQuantityForIngredient < availableQuantity) {
                availableQuantity = dishQuantityForIngredient;
            }
        }

        return availableQuantity == Double.MAX_VALUE ? 0 : availableQuantity;
    }
}