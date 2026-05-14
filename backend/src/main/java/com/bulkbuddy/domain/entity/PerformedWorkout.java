package com.bulkbuddy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Records a specific workout performed by a user on a given day.
 * <p>
 * Links to a standardized {@link Workout} template and a {@link DailyLog}.
 * {@code quantity} acts as a multiplier for the workout's
 * {@code caloriesBurned} value (e.g., 1.5 hours → quantity = 1.5 equivalent,
 * but stored as integer units for simplicity — see service layer for
 * fractional-hour support via minutes).
 * </p>
 */
@Entity
@Table(name = "performed_workouts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformedWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_log_id", nullable = false)
    private DailyLog dailyLog;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    /**
     * Duration multiplier — represents units of time (e.g., number of hours
     * or sessions). Acts as a multiplier for {@code workout.caloriesBurned}.
     * Stored as minutes to support fractional hours (90 min = 1.5 h).
     */
    @NotNull(message = "Duration in minutes is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Column(nullable = false)
    private Integer durationMinutes;

    // ── Derived helpers ──────────────────────────────────────────────

    /**
     * Total calories burned from this performed workout entry.
     * Calculation: (caloriesBurned per hour) × (durationMinutes / 60).
     */
    public int getTotalCaloriesBurned() {
        return (int) Math.round(workout.getCaloriesBurned() * (durationMinutes / 60.0));
    }
}
