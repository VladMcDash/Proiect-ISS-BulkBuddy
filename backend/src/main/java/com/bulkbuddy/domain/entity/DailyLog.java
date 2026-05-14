package com.bulkbuddy.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's daily tracking log.
 * <p>
 * A unique {@code DailyLog} is created for each (user, date) pair.
 * It aggregates consumed meals and performed workouts, and maintains
 * running totals of calories consumed/burned and protein consumed.
 * </p>
 */
@Entity
@Table(
    name = "daily_logs",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalCaloriesConsumed = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalProteinConsumed = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalCaloriesBurned = 0;

    @OneToMany(mappedBy = "dailyLog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ConsumedMeal> consumedMeals = new ArrayList<>();

    @OneToMany(mappedBy = "dailyLog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PerformedWorkout> performedWorkouts = new ArrayList<>();

    // ── Business Logic ───────────────────────────────────────────────

    /**
     * Checks whether the user has met <em>both</em> their daily calorie
     * and protein goals based on the current log totals.
     *
     * @return {@code true} if both goals are satisfied
     */
    public boolean checkGoals() {
        if (user == null) {
            return false;
        }
        int remainingCalories = user.getRemainingCalories(totalCaloriesConsumed, totalCaloriesBurned);
        boolean calorieGoalMet = remainingCalories <= 0;
        boolean proteinGoalMet = totalProteinConsumed >= user.getDailyProteinGoal();
        return calorieGoalMet && proteinGoalMet;
    }
}
