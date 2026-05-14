package com.bulkbuddy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Records a specific meal consumed by a user on a given day.
 * <p>
 * Links to a standardized {@link Meal} template and a {@link DailyLog}.
 * {@code quantity} represents the number of 100 g portions consumed.
 * Actual calories = meal.calories × quantity.
 * Actual protein  = meal.protein  × quantity.
 * </p>
 */
@Entity
@Table(name = "consumed_meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumedMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_log_id", nullable = false)
    private DailyLog dailyLog;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    /** Quantity in grams */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity;

    // ── Derived helpers ──────────────────────────────────────────────

    /** Total calories from this consumed meal entry. */
    public int getTotalCalories() {
        return (int) Math.round((meal.getCalories() * quantity) / 100.0);
    }

    /** Total protein from this consumed meal entry. */
    public int getTotalProtein() {
        return (int) Math.round((meal.getProtein() * quantity) / 100.0);
    }
}
