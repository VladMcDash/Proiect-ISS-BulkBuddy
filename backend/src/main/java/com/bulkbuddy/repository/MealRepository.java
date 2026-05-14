package com.bulkbuddy.repository;

import com.bulkbuddy.domain.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Meal} entities.
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    /**
     * Search meals whose name contains the given string (case-insensitive).
     * Powers the user-facing meal search bar.
     *
     * @param name partial name to search for
     * @return list of matching meals
     */
    List<Meal> findByNameContainingIgnoreCase(String name);

    /**
     * Find a meal by its exact name (case-insensitive).
     * Used during CSV import to prevent duplicates.
     *
     * @param name exact meal name
     * @return an Optional containing the meal, or empty if not found
     */
    Optional<Meal> findByNameIgnoreCase(String name);

    /**
     * Check whether a meal with the given name already exists (case-insensitive).
     *
     * @param name the meal name to check
     * @return true if a meal with this name exists
     */
    boolean existsByNameIgnoreCase(String name);
}
