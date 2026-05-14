package com.bulkbuddy.dto.mapper;

import com.bulkbuddy.domain.entity.ConsumedMeal;
import com.bulkbuddy.domain.entity.DailyLog;
import com.bulkbuddy.domain.entity.Meal;
import com.bulkbuddy.domain.entity.Notification;
import com.bulkbuddy.domain.entity.PerformedWorkout;
import com.bulkbuddy.domain.entity.User;
import com.bulkbuddy.domain.entity.Workout;
import com.bulkbuddy.dto.response.DailyLogResponse;
import com.bulkbuddy.dto.response.MealResponse;
import com.bulkbuddy.dto.response.NotificationResponse;
import com.bulkbuddy.dto.response.UserResponse;
import com.bulkbuddy.dto.response.WorkoutResponse;
import com.bulkbuddy.service.DailyLogService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps domain entities to response DTOs.
 * Centralizes all entity→DTO conversion logic.
 */
@Component
public class DtoMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .dailyCalorieGoal(user.getDailyCalorieGoal())
                .dailyProteinGoal(user.getDailyProteinGoal())
                .build();
    }

    public MealResponse toMealResponse(Meal meal) {
        return MealResponse.builder()
                .id(meal.getId())
                .name(meal.getName())
                .calories(meal.getCalories())
                .protein(meal.getProtein())
                .imageUrl(meal.getImageUrl())
                .build();
    }

    public List<MealResponse> toMealResponseList(List<Meal> meals) {
        return meals.stream().map(this::toMealResponse).collect(Collectors.toList());
    }

    public WorkoutResponse toWorkoutResponse(Workout workout) {
        return WorkoutResponse.builder()
                .id(workout.getId())
                .type(workout.getType())
                .caloriesBurned(workout.getCaloriesBurned())
                .build();
    }

    public List<WorkoutResponse> toWorkoutResponseList(List<Workout> workouts) {
        return workouts.stream().map(this::toWorkoutResponse).collect(Collectors.toList());
    }

    public NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .date(notification.getDate())
                .build();
    }

    public List<NotificationResponse> toNotificationResponseList(List<Notification> notifications) {
        return notifications.stream().map(this::toNotificationResponse).collect(Collectors.toList());
    }

    public DailyLogResponse toDailyLogResponse(DailyLogService.DailyLogSummary summary) {
        DailyLog log = summary.dailyLog();

        List<DailyLogResponse.ConsumedMealEntry> mealEntries = log.getConsumedMeals() != null
                ? log.getConsumedMeals().stream().map(this::toConsumedMealEntry).collect(Collectors.toList())
                : List.of();

        List<DailyLogResponse.PerformedWorkoutEntry> workoutEntries = log.getPerformedWorkouts() != null
                ? log.getPerformedWorkouts().stream().map(this::toPerformedWorkoutEntry).collect(Collectors.toList())
                : List.of();

        return DailyLogResponse.builder()
                .id(log.getId())
                .date(log.getDate())
                .calorieGoal(summary.calorieGoal())
                .proteinGoal(summary.proteinGoal())
                .caloriesConsumed(summary.caloriesConsumed())
                .caloriesBurned(summary.caloriesBurned())
                .proteinConsumed(summary.proteinConsumed())
                .remainingCalories(summary.remainingCalories())
                .remainingProtein(summary.remainingProtein())
                .goalsComplete(summary.goalsComplete())
                .consumedMeals(mealEntries)
                .performedWorkouts(workoutEntries)
                .build();
    }

    public DailyLogResponse toDailyLogResponse(DailyLog log, User user) {
        int remainingCalories = user.getRemainingCalories(
                log.getTotalCaloriesConsumed(), log.getTotalCaloriesBurned());
        int remainingProtein = Math.max(user.getDailyProteinGoal() - log.getTotalProteinConsumed(), 0);

        List<DailyLogResponse.ConsumedMealEntry> mealEntries = log.getConsumedMeals() != null
                ? log.getConsumedMeals().stream().map(this::toConsumedMealEntry).collect(Collectors.toList())
                : List.of();

        List<DailyLogResponse.PerformedWorkoutEntry> workoutEntries = log.getPerformedWorkouts() != null
                ? log.getPerformedWorkouts().stream().map(this::toPerformedWorkoutEntry).collect(Collectors.toList())
                : List.of();

        return DailyLogResponse.builder()
                .id(log.getId())
                .date(log.getDate())
                .calorieGoal(user.getDailyCalorieGoal())
                .proteinGoal(user.getDailyProteinGoal())
                .caloriesConsumed(log.getTotalCaloriesConsumed())
                .caloriesBurned(log.getTotalCaloriesBurned())
                .proteinConsumed(log.getTotalProteinConsumed())
                .remainingCalories(remainingCalories)
                .remainingProtein(remainingProtein)
                .goalsComplete(log.checkGoals())
                .consumedMeals(mealEntries)
                .performedWorkouts(workoutEntries)
                .build();
    }

    private DailyLogResponse.ConsumedMealEntry toConsumedMealEntry(ConsumedMeal cm) {
        return DailyLogResponse.ConsumedMealEntry.builder()
                .id(cm.getId())
                .mealId(cm.getMeal().getId())
                .mealName(cm.getMeal().getName())
                .mealImageUrl(cm.getMeal().getImageUrl())
                .caloriesPer100g(cm.getMeal().getCalories())
                .proteinPer100g(cm.getMeal().getProtein())
                .quantity(cm.getQuantity())
                .totalCalories(cm.getTotalCalories())
                .totalProtein(cm.getTotalProtein())
                .build();
    }

    private DailyLogResponse.PerformedWorkoutEntry toPerformedWorkoutEntry(PerformedWorkout pw) {
        return DailyLogResponse.PerformedWorkoutEntry.builder()
                .id(pw.getId())
                .workoutId(pw.getWorkout().getId())
                .workoutType(pw.getWorkout().getType())
                .caloriesBurnedPerHour(pw.getWorkout().getCaloriesBurned())
                .durationMinutes(pw.getDurationMinutes())
                .totalCaloriesBurned(pw.getTotalCaloriesBurned())
                .build();
    }
}
