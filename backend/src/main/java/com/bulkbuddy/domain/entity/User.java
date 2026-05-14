package com.bulkbuddy.domain.entity;

import com.bulkbuddy.domain.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Represents a registered user in BulkBuddy.
 * <p>
 * Each user has a role (ADMIN or USER), personal daily macro goals,
 * and credentials for authentication.
 * </p>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @NotNull(message = "Daily calorie goal is required")
    @Min(value = 0, message = "Daily calorie goal must be non-negative")
    @Column(nullable = false)
    private Integer dailyCalorieGoal;

    @NotNull(message = "Daily protein goal is required")
    @Min(value = 0, message = "Daily protein goal must be non-negative")
    @Column(nullable = false)
    private Integer dailyProteinGoal;

    // ── Business Logic ───────────────────────────────────────────────

    /**
     * Calculates the remaining calories the user still needs to consume today.
     *
     * @param consumed total calories consumed so far
     * @param burned   total calories burned through workouts
     * @return remaining calories (goal − consumed + burned)
     */
    public int getRemainingCalories(int consumed, int burned) {
        return dailyCalorieGoal - consumed + burned;
    }
}
