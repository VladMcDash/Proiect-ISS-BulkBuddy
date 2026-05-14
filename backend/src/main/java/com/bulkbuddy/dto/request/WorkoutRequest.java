package com.bulkbuddy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for creating/updating a standardized Workout.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRequest {

    @NotBlank(message = "Workout type is required")
    private String type;

    @NotNull(message = "Calories burned is required")
    @Min(value = 0, message = "Calories burned must be non-negative")
    private Integer caloriesBurned;
}
