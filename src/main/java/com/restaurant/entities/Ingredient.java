package com.restaurant.entities;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Ingredient {
    private int id;
    private String name;
    private double unitPrice;
    private Unit unit;
    private LocalDateTime updateDateTime;
    private double requiredQuantity;

    public Ingredient(int id, String name, double unitPrice, Unit unit, LocalDateTime updateDateTime, double requiredQuantity) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.updateDateTime = updateDateTime;
        this.requiredQuantity = requiredQuantity;
    }

    public Ingredient() {
    }
}
