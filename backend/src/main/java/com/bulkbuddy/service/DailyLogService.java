package com.bulkbuddy.service;

import com.bulkbuddy.domain.entity.ConsumedMeal;
import com.bulkbuddy.domain.entity.DailyLog;
import com.bulkbuddy.domain.entity.Meal;
import com.bulkbuddy.domain.entity.PerformedWorkout;
import com.bulkbuddy.domain.entity.User;
import com.bulkbuddy.domain.entity.Workout;
import com.bulkbuddy.repository.ConsumedMealRepository;
import com.bulkbuddy.repository.DailyLogRepository;
import com.bulkbuddy.repository.PerformedWorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Core service for daily tracking operations.
 * <p>
 * Manages the lifecycle of {@link DailyLog} entries and handles:
 * <ul>
 *   <li>UC3: Adding consumed meals to the daily log</li>
 *   <li>UC5: Adding performed workouts to the daily log</li>
 *   <li>UC6: Checking goals and triggering notifications</li>
 *   <li>Live recalculation of totals after every change</li>
 * </ul>
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final ConsumedMealRepository consumedMealRepository;
    private final PerformedWorkoutRepository performedWorkoutRepository;
    private final UserService userService;
    private final MealService mealService;
    private final WorkoutService workoutService;
    private final NotificationService notificationService;

    // ── DailyLog Lifecycle ───────────────────────────────────────────

    /**
     * Get or create today's DailyLog for a user.
     * Each daily login/session creates a fresh log if one doesn't exist yet.
     *
     * @param userId the user's ID
     * @return the existing or newly created DailyLog for today
     */
    public DailyLog getOrCreateTodayLog(Long userId) {
        User user = userService.getUserById(userId);
        LocalDate today = LocalDate.now();

        return dailyLogRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> {
                    DailyLog newLog = DailyLog.builder()
                            .user(user)
                            .date(today)
                            .totalCaloriesConsumed(0)
                            .totalProteinConsumed(0)
                            .totalCaloriesBurned(0)
                            .build();
                    DailyLog saved = dailyLogRepository.save(newLog);
                    log.info("Created new DailyLog for user {} on {}", user.getUsername(), today);
                    return saved;
                });
    }

    /**
     * Get a specific DailyLog by ID.
     */
    @Transactional(readOnly = true)
    public DailyLog getDailyLogById(Long logId) {
        return dailyLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "DailyLog not found with ID: " + logId));
    }

    /**
     * Get a user's daily log history, most recent first.
     */
    @Transactional(readOnly = true)
    public List<DailyLog> getUserLogHistory(Long userId) {
        User user = userService.getUserById(userId);
        return dailyLogRepository.findByUserOrderByDateDesc(user);
    }

    // ── UC3: Add Consumed Meal ───────────────────────────────────────

    /**
     * UC3: User selects a meal they ate — adds it to today's DailyLog
     * and recalculates all totals.
     *
     * @param userId   the user's ID
     * @param mealId   the standardized meal's ID
     * @param quantityGrams exact weight in grams consumed
     * @return the updated DailyLog
     */
    public DailyLog addConsumedMeal(Long userId, Long mealId, double quantityGrams) {
        if (quantityGrams <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        DailyLog dailyLog = getOrCreateTodayLog(userId);
        Meal meal = mealService.getMealById(mealId);

        ConsumedMeal consumedMeal = ConsumedMeal.builder()
                .dailyLog(dailyLog)
                .meal(meal)
                .quantity(quantityGrams)
                .build();

        consumedMealRepository.save(consumedMeal);
        log.info("User {} consumed {}g {} ({}kcal, {}g protein)",
                dailyLog.getUser().getUsername(), quantityGrams, meal.getName(),
                consumedMeal.getTotalCalories(), consumedMeal.getTotalProtein());

        // Recalculate totals
        recalculateTotals(dailyLog);

        // UC6: Check goals and notify
        checkAndNotifyGoals(dailyLog);

        return dailyLog;
    }

    /**
     * Remove a consumed meal entry and recalculate totals.
     *
     * @param consumedMealId the consumed meal entry ID
     * @return the updated DailyLog
     */
    public DailyLog removeConsumedMeal(Long consumedMealId) {
        ConsumedMeal consumedMeal = consumedMealRepository.findById(consumedMealId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ConsumedMeal not found with ID: " + consumedMealId));

        DailyLog dailyLog = consumedMeal.getDailyLog();
        consumedMealRepository.delete(consumedMeal);

        recalculateTotals(dailyLog);
        return dailyLog;
    }

    // ── UC5: Add Performed Workout ───────────────────────────────────

    /**
     * UC5: User selects a workout they did today — adds it to today's
     * DailyLog and recalculates burned calories.
     *
     * @param userId          the user's ID
     * @param workoutId       the standardized workout's ID
     * @param durationMinutes how long the workout was performed (in minutes)
     * @return the updated DailyLog
     */
    public DailyLog addPerformedWorkout(Long userId, Long workoutId, int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }

        DailyLog dailyLog = getOrCreateTodayLog(userId);
        Workout workout = workoutService.getWorkoutById(workoutId);

        PerformedWorkout performedWorkout = PerformedWorkout.builder()
                .dailyLog(dailyLog)
                .workout(workout)
                .durationMinutes(durationMinutes)
                .build();

        performedWorkoutRepository.save(performedWorkout);
        log.info("User {} performed {} for {}min (burned {}kcal)",
                dailyLog.getUser().getUsername(), workout.getType(),
                durationMinutes, performedWorkout.getTotalCaloriesBurned());

        // Recalculate totals
        recalculateTotals(dailyLog);

        // UC6: Check goals and notify (workouts increase remaining, so re-check)
        checkAndNotifyGoals(dailyLog);

        return dailyLog;
    }

    /**
     * Remove a performed workout entry and recalculate totals.
     *
     * @param performedWorkoutId the performed workout entry ID
     * @return the updated DailyLog
     */
    public DailyLog removePerformedWorkout(Long performedWorkoutId) {
        PerformedWorkout performedWorkout = performedWorkoutRepository.findById(performedWorkoutId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "PerformedWorkout not found with ID: " + performedWorkoutId));

        DailyLog dailyLog = performedWorkout.getDailyLog();
        performedWorkoutRepository.delete(performedWorkout);

        recalculateTotals(dailyLog);
        return dailyLog;
    }

    // ── Totals Recalculation ─────────────────────────────────────────

    /**
     * Recalculates all aggregate totals on the DailyLog by summing
     * individual consumed meals and performed workouts.
     * This ensures the tracker is always "live" after any change.
     */
    private void recalculateTotals(DailyLog dailyLog) {
        // Fetch fresh lists from DB to ensure accuracy
        List<ConsumedMeal> meals = consumedMealRepository.findByDailyLogId(dailyLog.getId());
        List<PerformedWorkout> workouts = performedWorkoutRepository.findByDailyLogId(dailyLog.getId());

        int totalCaloriesConsumed = meals.stream()
                .mapToInt(ConsumedMeal::getTotalCalories)
                .sum();

        int totalProteinConsumed = meals.stream()
                .mapToInt(ConsumedMeal::getTotalProtein)
                .sum();

        int totalCaloriesBurned = workouts.stream()
                .mapToInt(PerformedWorkout::getTotalCaloriesBurned)
                .sum();

        dailyLog.setTotalCaloriesConsumed(totalCaloriesConsumed);
        dailyLog.setTotalProteinConsumed(totalProteinConsumed);
        dailyLog.setTotalCaloriesBurned(totalCaloriesBurned);

        dailyLogRepository.save(dailyLog);

        log.debug("Recalculated DailyLog {}: consumed={}kcal, protein={}g, burned={}kcal",
                dailyLog.getId(), totalCaloriesConsumed, totalProteinConsumed, totalCaloriesBurned);
    }

    // ── UC6: Goal Checking & Notifications ───────────────────────────

    /**
     * UC6: Checks whether the user has met their daily goals and
     * creates a congratulatory notification if so.
     */
    private void checkAndNotifyGoals(DailyLog dailyLog) {
        User user = dailyLog.getUser();
        int remainingCalories = user.getRemainingCalories(
                dailyLog.getTotalCaloriesConsumed(),
                dailyLog.getTotalCaloriesBurned()
        );

        boolean calorieGoalMet = remainingCalories <= 0;
        boolean proteinGoalMet = dailyLog.getTotalProteinConsumed() >= user.getDailyProteinGoal();

        if (calorieGoalMet && proteinGoalMet) {
            notificationService.createNotification(user,
                    "Great job! You've reached BOTH your daily calorie and protein goals today!");
        } else if (calorieGoalMet) {
            notificationService.createNotification(user,
                    "You've hit your daily calorie goal! Keep going for protein!");
        } else if (proteinGoalMet) {
            notificationService.createNotification(user,
                    "Protein goal reached! Keep eating to hit your calorie target!");
        }
    }

    // ── Summary / Dashboard Data ─────────────────────────────────────

    /**
     * Get a summary of today's tracking status for a user.
     * Returns the DailyLog with all calculated totals.
     */
    @Transactional(readOnly = true)
    public DailyLogSummary getTodaySummary(Long userId) {
        User user = userService.getUserById(userId);
        DailyLog dailyLog = getOrCreateTodayLog(userId);

        int remainingCalories = user.getRemainingCalories(
                dailyLog.getTotalCaloriesConsumed(),
                dailyLog.getTotalCaloriesBurned()
        );

        int remainingProtein = user.getDailyProteinGoal() - dailyLog.getTotalProteinConsumed();

        boolean goalsComplete = dailyLog.checkGoals();

        return new DailyLogSummary(
                dailyLog,
                user.getDailyCalorieGoal(),
                user.getDailyProteinGoal(),
                dailyLog.getTotalCaloriesConsumed(),
                dailyLog.getTotalCaloriesBurned(),
                dailyLog.getTotalProteinConsumed(),
                remainingCalories,
                Math.max(remainingProtein, 0),
                goalsComplete
        );
    }

    /**
     * Data record holding a complete summary of today's tracking status.
     */
    public record DailyLogSummary(
            DailyLog dailyLog,
            int calorieGoal,
            int proteinGoal,
            int caloriesConsumed,
            int caloriesBurned,
            int proteinConsumed,
            int remainingCalories,
            int remainingProtein,
            boolean goalsComplete
    ) {}
}
