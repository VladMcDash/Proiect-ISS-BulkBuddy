package com.bulkbuddy.controller;

import com.bulkbuddy.domain.entity.Workout;
import com.bulkbuddy.dto.mapper.DtoMapper;
import com.bulkbuddy.dto.request.WorkoutRequest;
import com.bulkbuddy.dto.response.WorkoutResponse;
import com.bulkbuddy.service.WorkoutService;
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
 * Admin controller for managing standardized workouts (CRUD).
 * UC4: Admin adds workouts to DB.
 */
@RestController
@RequestMapping("/api/admin/workouts")
@RequiredArgsConstructor
public class AdminWorkoutController {

    private final WorkoutService workoutService;
    private final DtoMapper dtoMapper;

    /**
     * POST /api/admin/workouts — Create a new standardized workout.
     */
    @PostMapping
    public ResponseEntity<?> createWorkout(@Valid @RequestBody WorkoutRequest request) {
        try {
            Workout workout = workoutService.createWorkout(
                    request.getType(),
                    request.getCaloriesBurned()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toWorkoutResponse(workout));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/admin/workouts/{id} — Update an existing workout.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkout(@PathVariable Long id,
                                           @Valid @RequestBody WorkoutRequest request) {
        try {
            Workout workout = workoutService.updateWorkout(
                    id,
                    request.getType(),
                    request.getCaloriesBurned()
            );
            return ResponseEntity.ok(dtoMapper.toWorkoutResponse(workout));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/admin/workouts/{id} — Delete a workout.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable Long id) {
        try {
            workoutService.deleteWorkout(id);
            return ResponseEntity.ok(Map.of("message", "Workout deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/admin/workouts — List all workouts (admin view).
     */
    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getAllWorkouts() {
        return ResponseEntity.ok(dtoMapper.toWorkoutResponseList(workoutService.getAllWorkouts()));
    }
}
