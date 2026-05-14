package com.bulkbuddy.service;

import com.bulkbuddy.domain.entity.Meal;
import com.bulkbuddy.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for standardized {@link Meal} management.
 * Only Admins should invoke create/update/delete operations (enforced at Controller level).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MealService {

    private final MealRepository mealRepository;

    /**
     * UC1: Admin adds a new standardized meal to the database.
     *
     * @param name     meal name
     * @param calories calories per 100 g
     * @param protein  protein grams per 100 g
     * @param imageUrl optional URL to meal photo
     * @return the persisted Meal
     */
    public Meal createMeal(String name, int calories, int protein, String imageUrl) {
        if (mealRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Meal '" + name + "' already exists.");
        }

        Meal meal = Meal.builder()
                .name(name)
                .calories(calories)
                .protein(protein)
                .imageUrl(imageUrl)
                .build();

        Meal saved = mealRepository.save(meal);
        log.info("Created meal: {} ({}kcal, {}g protein per 100g)", name, calories, protein);
        return saved;
    }

    /**
     * Update an existing meal's details.
     */
    public Meal updateMeal(Long id, String name, int calories, int protein, String imageUrl) {
        Meal meal = getMealById(id);
        meal.setName(name);
        meal.setCalories(calories);
        meal.setProtein(protein);
        meal.setImageUrl(imageUrl);
        log.info("Updated meal ID {}: {}", id, name);
        return mealRepository.save(meal);
    }

    /**
     * Delete a meal by ID.
     */
    public void deleteMeal(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new IllegalArgumentException("Meal not found with ID: " + id);
        }
        mealRepository.deleteById(id);
        log.info("Deleted meal ID {}", id);
    }

    /**
     * Get a meal by ID.
     */
    @Transactional(readOnly = true)
    public Meal getMealById(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found with ID: " + id));
    }

    /**
     * Get all meals in the standardized database.
     */
    @Transactional(readOnly = true)
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    /**
     * Search meals by name (case-insensitive partial match).
     * Powers the user-facing search bar.
     */
    @Transactional(readOnly = true)
    public List<Meal> searchMeals(String query) {
        if (query == null || query.isBlank()) {
            return mealRepository.findAll();
        }
        return mealRepository.findByNameContainingIgnoreCase(query.trim());
    }
}
