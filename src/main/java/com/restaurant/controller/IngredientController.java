package com.restaurant.controller;

import com.restaurant.entities.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.restaurant.entities.Unit;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private static final List<Ingredient> ingredients = new ArrayList<>();

    static {
        ingredients.add(new Ingredient(1, "Oeuf", 1000.0, Unit.U,
                LocalDateTime.of(2025, 3, 1, 0, 0).atOffset(ZoneOffset.UTC).toLocalDateTime(), 0));
        ingredients.add(new Ingredient(2, "Huile", 10000.0, Unit.L,
                LocalDateTime.of(2025, 3, 20, 0, 0).atOffset(ZoneOffset.UTC).toLocalDateTime(), 0));
        ingredients.add(new Ingredient(3, "Tomate", 500.0, Unit.G,
                LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime(), 0));
        ingredients.add(new Ingredient(4, "Fromage", 800.0, Unit.G,
                LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime(), 0));
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping(params = {"priceMinFilter", "priceMaxFilter"})
    public ResponseEntity<?> getIngredientsByPriceRange(
            @RequestParam(required = false) Double priceMinFilter,
            @RequestParam(required = false) Double priceMaxFilter) {

        if (priceMinFilter != null && priceMinFilter < 0) {
            return ResponseEntity.badRequest().body("priceMinFilter ne peut pas être négatif");
        }

        if (priceMaxFilter != null && priceMaxFilter < 0) {
            return ResponseEntity.badRequest().body("priceMaxFilter ne peut pas être négatif");
        }

        if (priceMinFilter != null && priceMaxFilter != null && priceMinFilter > priceMaxFilter) {
            return ResponseEntity.badRequest().body(
                    String.format("priceMinFilter (%s) ne peut pas être supérieur à priceMaxFilter (%s)",
                            priceMinFilter, priceMaxFilter));
        }

        List<Ingredient> filteredIngredients = ingredients.stream()
                .filter(ingredient ->
                        (priceMinFilter == null || ingredient.getUnitPrice() >= priceMinFilter) &&
                                (priceMaxFilter == null || ingredient.getUnitPrice() <= priceMaxFilter))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredIngredients);
    }

    @PostMapping
    public ResponseEntity<List<Ingredient>> createIngredients(@RequestBody List<Ingredient> newIngredients) {
        int maxId = ingredients.stream()
                .mapToInt(Ingredient::getId)
                .max()
                .orElse(0);

        for (Ingredient ingredient : newIngredients) {
            ingredient.setId(++maxId);
            ingredient.setUpdateDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
            ingredients.add(ingredient);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newIngredients);
    }

    @PutMapping
    public ResponseEntity<List<Ingredient>> updateIngredients(@RequestBody List<Ingredient> updatedIngredients) {
        for (Ingredient updatedIngredient : updatedIngredients) {
            ingredients.stream()
                    .filter(i -> i.getId() == updatedIngredient.getId())
                    .findFirst()
                    .ifPresent(existingIngredient -> {
                        existingIngredient.setName(updatedIngredient.getName());
                        existingIngredient.setUnitPrice(updatedIngredient.getUnitPrice());
                        existingIngredient.setUpdateDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
                    });
        }

        return ResponseEntity.ok(updatedIngredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable int id) {
        Optional<Ingredient> ingredient = ingredients.stream()
                .filter(i -> i.getId() == id)
                .findFirst();

        if (ingredient.isPresent()) {
            return ResponseEntity.ok(ingredient.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Ingredient=%d is not found", id));
        }
    }
}