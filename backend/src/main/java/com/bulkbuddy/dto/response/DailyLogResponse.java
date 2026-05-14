package com.bulkbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Response body for the daily log summary — the main dashboard data object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyLogResponse {
    private Long id;
    private LocalDate date;

    // Goals
    private Integer calorieGoal;
    private Integer proteinGoal;

    // Totals
    private Integer caloriesConsumed;
    private Integer caloriesBurned;
    private Integer proteinConsumed;

    // Remaining
    private Integer remainingCalories;
    private Integer remainingProtein;

    // Status
    private Boolean goalsComplete;

    // Detail lists
    private List<ConsumedMealEntry> consumedMeals;
    private List<PerformedWorkoutEntry> performedWorkouts;

    /**
     * Nested DTO for a consumed meal entry within the daily log.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConsumedMealEntry {
        private Long id;
        private Long mealId;
        private String mealName;
        private String mealImageUrl;
        private Integer caloriesPer100g;
        private Integer proteinPer100g;
        private Double quantity;
        private Integer totalCalories;
        private Integer totalProtein;
    }

    /**
     * Nested DTO for a performed workout entry within the daily log.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PerformedWorkoutEntry {
        private Long id;
        private Long workoutId;
        private String workoutType;
        private Integer caloriesBurnedPerHour;
        private Integer durationMinutes;
        private Integer totalCaloriesBurned;
    }
}
