package com.bulkbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response body for meal data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealResponse {
    private Long id;
    private String name;
    private Integer calories;
    private Integer protein;
    private String imageUrl;
}
