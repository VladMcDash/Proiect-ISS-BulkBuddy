package com.bulkbuddy.repository;

import com.bulkbuddy.domain.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Workout} entities.
 */
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    /**
     * Search workouts whose type contains the given string (case-insensitive).
     *
     * @param type partial type name to search for
     * @return list of matching workouts
     */
    List<Workout> findByTypeContainingIgnoreCase(String type);

    /**
     * Find a workout by its exact type name (case-insensitive).
     * Used during CSV import to prevent duplicates.
     *
     * @param type exact workout type name
     * @return an Optional containing the workout, or empty if not found
     */
    Optional<Workout> findByTypeIgnoreCase(String type);

    /**
     * Check whether a workout with the given type already exists (case-insensitive).
     *
     * @param type the workout type to check
     * @return true if a workout with this type exists
     */
    boolean existsByTypeIgnoreCase(String type);
}
