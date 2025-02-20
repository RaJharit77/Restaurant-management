package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ingredient {
    private int id;
    private String name;
    private double unitPrice;
    private Unit unit;
    private LocalDateTime updateDateTime;

    public Ingredient(int id, String name, double unitPrice, Unit unit, LocalDateTime updateDateTime) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.updateDateTime = updateDateTime;
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

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", unit=" + unit +
                ", updateDateTime=" + updateDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id && Double.compare(unitPrice, that.unitPrice) == 0 && Objects.equals(name, that.name) && unit == that.unit && Objects.equals(updateDateTime, that.updateDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitPrice, unit, updateDateTime);
    }
}
