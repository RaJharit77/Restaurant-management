package com.restaurant.controller;

import com.restaurant.entities.Dish;
import com.restaurant.services.DishServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishServices dishService;

    public DishController(DishServices dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable int id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @PostMapping
    public ResponseEntity<List<Dish>> saveDishes(@RequestBody List<Dish> dishes) {
        return ResponseEntity.ok(dishService.saveAllDishes(dishes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable int id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Dish>> filterDishes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double unitPrice,
            @RequestParam(required = false) List<Integer> ingredientIds) {
        // filtrage
        return ResponseEntity.ok(dishService.filterDishes(name, unitPrice, null));
    }
}