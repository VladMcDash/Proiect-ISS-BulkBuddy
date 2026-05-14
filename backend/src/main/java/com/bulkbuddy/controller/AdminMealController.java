package com.bulkbuddy.controller;

import com.bulkbuddy.domain.entity.Meal;
import com.bulkbuddy.dto.mapper.DtoMapper;
import com.bulkbuddy.dto.request.MealRequest;
import com.bulkbuddy.dto.response.MealResponse;
import com.bulkbuddy.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Admin controller for managing standardized meals (CRUD).
 * UC1: Admin adds meals to DB.
 */
@RestController
@RequestMapping("/api/admin/meals")
@RequiredArgsConstructor
public class AdminMealController {

    private final MealService mealService;
    private final DtoMapper dtoMapper;

    /**
     * POST /api/admin/meals — Create a new standardized meal.
     */
    @PostMapping
    public ResponseEntity<?> createMeal(@Valid @RequestBody MealRequest request) {
        try {
            Meal meal = mealService.createMeal(
                    request.getName(),
                    request.getCalories(),
                    request.getProtein(),
                    request.getImageUrl()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toMealResponse(meal));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/admin/meals/{id} — Update an existing meal.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeal(@PathVariable Long id,
                                        @Valid @RequestBody MealRequest request) {
        try {
            Meal meal = mealService.updateMeal(
                    id,
                    request.getName(),
                    request.getCalories(),
                    request.getProtein(),
                    request.getImageUrl()
            );
            return ResponseEntity.ok(dtoMapper.toMealResponse(meal));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/admin/meals/{id} — Delete a meal.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable Long id) {
        try {
            mealService.deleteMeal(id);
            return ResponseEntity.ok(Map.of("message", "Meal deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/admin/meals — List all meals (admin view).
     */
    @GetMapping
    public ResponseEntity<List<MealResponse>> getAllMeals() {
        return ResponseEntity.ok(dtoMapper.toMealResponseList(mealService.getAllMeals()));
    }
}
