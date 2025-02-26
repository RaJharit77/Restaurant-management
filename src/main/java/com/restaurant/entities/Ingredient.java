package com.restaurant.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private List<PriceHistory> priceHistory;
    private List<StockMovement> stockMovements;

    public Ingredient() {
        this.priceHistory = new ArrayList<>();
        this.stockMovements = new ArrayList<>();
    }

    public Ingredient(int id, String name, double unitPrice, Unit unit, LocalDateTime updateDateTime, double requiredQuantity) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.updateDateTime = updateDateTime;
        this.requiredQuantity = requiredQuantity;
        this.priceHistory = new ArrayList<>();
        this.stockMovements = new ArrayList<>();
        this.priceHistory.add(new PriceHistory(unitPrice, updateDateTime));
    }

    public void addPriceHistory(double price, LocalDateTime date) {
        this.priceHistory.add(new PriceHistory(price, date));
        this.unitPrice = price;
        this.updateDateTime = date;
    }

    public double getPriceAtDate(LocalDateTime date) {
        return priceHistory.stream()
                .filter(ph -> ph.getDate().isBefore(date) || ph.getDate().isEqual(date))
                .max((ph1, ph2) -> ph1.getDate().compareTo(ph2.getDate()))
                .map(PriceHistory::getPrice)
                .orElse(0.0);
    }

    public double getAvailableQuantity(LocalDateTime date) {
        double availableQuantity = 0;
        for (StockMovement movement : stockMovements) {
            if (movement.getMovementDate().isBefore(date)){
                if (movement.getMovementType() == MovementType.ENTRY) {
                    availableQuantity += movement.getQuantity();
                } else if (movement.getMovementType() == MovementType.EXIT) {
                    availableQuantity -= movement.getQuantity();
                }
            }
        }
        return availableQuantity;
    }
}