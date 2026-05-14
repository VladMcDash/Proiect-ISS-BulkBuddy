package com.bulkbuddy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Standardized workout template managed by Admins.
 * <p>
 * {@code caloriesBurned} represents the number of calories burned
 * <strong>per 1 unit of quantity/duration</strong> (e.g., per 1 hour).
 * When a user logs a workout, they specify a multiplier (quantity)
 * so that total burn = caloriesBurned × quantity.
 * </p>
 */
@Entity
@Table(name = "workouts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Workout type is required")
    @Column(nullable = false, length = 100)
    private String type;

    /** Calories burned per 1 unit of duration (e.g., per 1 hour) */
    @NotNull(message = "Calories burned value is required")
    @Min(value = 0, message = "Calories burned must be non-negative")
    @Column(nullable = false)
    private Integer caloriesBurned;
}
