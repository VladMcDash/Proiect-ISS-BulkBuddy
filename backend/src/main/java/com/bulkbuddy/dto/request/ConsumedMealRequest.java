package com.bulkbuddy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for adding a consumed meal to the daily log.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumedMealRequest {

    @NotNull(message = "Meal ID is required")
    private Long mealId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Double quantity;
}
