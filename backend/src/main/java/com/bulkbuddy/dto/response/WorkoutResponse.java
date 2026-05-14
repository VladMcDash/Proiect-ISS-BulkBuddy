package com.bulkbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response body for workout data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResponse {
    private Long id;
    private String type;
    private Integer caloriesBurned;
}
