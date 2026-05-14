package com.bulkbuddy.service;

import com.bulkbuddy.domain.entity.Workout;
import com.bulkbuddy.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for standardized {@link Workout} management.
 * Only Admins should invoke create/update/delete operations (enforced at Controller level).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    /**
     * UC4: Admin adds a new standardized workout to the database.
     *
     * @param type           workout type name (e.g., "Running", "Cycling")
     * @param caloriesBurned calories burned per 1 hour
     * @return the persisted Workout
     */
    public Workout createWorkout(String type, int caloriesBurned) {
        if (workoutRepository.existsByTypeIgnoreCase(type)) {
            throw new IllegalArgumentException("Workout '" + type + "' already exists.");
        }

        Workout workout = Workout.builder()
                .type(type)
                .caloriesBurned(caloriesBurned)
                .build();

        Workout saved = workoutRepository.save(workout);
        log.info("Created workout: {} ({}kcal/h)", type, caloriesBurned);
        return saved;
    }

    /**
     * Update an existing workout's details.
     */
    public Workout updateWorkout(Long id, String type, int caloriesBurned) {
        Workout workout = getWorkoutById(id);
        workout.setType(type);
        workout.setCaloriesBurned(caloriesBurned);
        log.info("Updated workout ID {}: {} ({}kcal/h)", id, type, caloriesBurned);
        return workoutRepository.save(workout);
    }

    /**
     * Delete a workout by ID.
     */
    public void deleteWorkout(Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new IllegalArgumentException("Workout not found with ID: " + id);
        }
        workoutRepository.deleteById(id);
        log.info("Deleted workout ID {}", id);
    }

    /**
     * Get a workout by ID.
     */
    @Transactional(readOnly = true)
    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found with ID: " + id));
    }

    /**
     * Get all workouts in the standardized database.
     */
    @Transactional(readOnly = true)
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    /**
     * Search workouts by type (case-insensitive partial match).
     */
    @Transactional(readOnly = true)
    public List<Workout> searchWorkouts(String query) {
        if (query == null || query.isBlank()) {
            return workoutRepository.findAll();
        }
        return workoutRepository.findByTypeContainingIgnoreCase(query.trim());
    }
}
