package com.bulkbuddy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for adding a performed workout to the daily log.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformedWorkoutRequest {

    @NotNull(message = "Workout ID is required")
    private Long workoutId;

    @NotNull(message = "Duration in minutes is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;
}
