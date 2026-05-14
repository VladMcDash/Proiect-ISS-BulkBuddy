package com.bulkbuddy.controller;

import com.bulkbuddy.dto.mapper.DtoMapper;
import com.bulkbuddy.dto.response.MealResponse;
import com.bulkbuddy.dto.response.WorkoutResponse;
import com.bulkbuddy.service.MealService;
import com.bulkbuddy.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Shared read-only endpoints for browsing the standardized
 * meals and workouts catalog. Accessible by all authenticated users.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {

    private final MealService mealService;
    private final WorkoutService workoutService;
    private final DtoMapper dtoMapper;

    /**
     * GET /api/meals — List or search standardized meals.
     *
     * @param q optional search query (partial name match)
     */
    @GetMapping("/meals")
    public ResponseEntity<List<MealResponse>> getMeals(
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(
                dtoMapper.toMealResponseList(mealService.searchMeals(q))
        );
    }

    /**
     * GET /api/workouts — List or search standardized workouts.
     *
     * @param q optional search query (partial type match)
     */
    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutResponse>> getWorkouts(
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(
                dtoMapper.toWorkoutResponseList(workoutService.searchWorkouts(q))
        );
    }
}
