package com.bulkbuddy.dto.response;

import com.bulkbuddy.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response body for user profile data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
    private Integer dailyCalorieGoal;
    private Integer dailyProteinGoal;
}
