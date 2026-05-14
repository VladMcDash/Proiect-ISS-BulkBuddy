package com.bulkbuddy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for updating a user's daily goals.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalUpdateRequest {

    @NotNull(message = "Daily calorie goal is required")
    @Min(value = 0, message = "Calorie goal must be non-negative")
    private Integer dailyCalorieGoal;

    @NotNull(message = "Daily protein goal is required")
    @Min(value = 0, message = "Protein goal must be non-negative")
    private Integer dailyProteinGoal;
}
