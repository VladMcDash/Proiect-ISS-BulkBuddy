package com.bulkbuddy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for creating/updating a standardized Meal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRequest {

    @NotBlank(message = "Meal name is required")
    private String name;

    @NotNull(message = "Calories is required")
    @Min(value = 0, message = "Calories must be non-negative")
    private Integer calories;

    @NotNull(message = "Protein is required")
    @Min(value = 0, message = "Protein must be non-negative")
    private Integer protein;

    private String imageUrl;
}
