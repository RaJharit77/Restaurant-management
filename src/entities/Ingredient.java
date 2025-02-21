package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ingredient {
    private int id;
    private String name;
    private double unitPrice;
    private Unit unit;
    private LocalDateTime updateDateTime;
    private double requiredQuantity;

    public Ingredient() {
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public double getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", unit=" + unit +
                ", updateDateTime=" + updateDateTime +
                ", requiredQuantity=" + requiredQuantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id && Double.compare(unitPrice, that.unitPrice) == 0 && Double.compare(requiredQuantity, that.requiredQuantity) == 0 && Objects.equals(name, that.name) && unit == that.unit && Objects.equals(updateDateTime, that.updateDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitPrice, unit, updateDateTime, requiredQuantity);
    }
}
