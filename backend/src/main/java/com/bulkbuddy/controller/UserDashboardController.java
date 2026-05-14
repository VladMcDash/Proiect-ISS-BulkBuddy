package com.bulkbuddy.controller;

import com.bulkbuddy.domain.entity.DailyLog;
import com.bulkbuddy.domain.entity.User;
import com.bulkbuddy.dto.mapper.DtoMapper;
import com.bulkbuddy.dto.request.ConsumedMealRequest;
import com.bulkbuddy.dto.request.GoalUpdateRequest;
import com.bulkbuddy.dto.request.PerformedWorkoutRequest;
import com.bulkbuddy.dto.response.DailyLogResponse;
import com.bulkbuddy.dto.response.UserResponse;
import com.bulkbuddy.service.DailyLogService;
import com.bulkbuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User-facing controller for daily tracking operations.
 * <p>
 * All endpoints extract the current user from the JWT token
 * (stored as credentials in the SecurityContext).
 * </p>
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserDashboardController {

    private final DailyLogService dailyLogService;
    private final UserService userService;
    private final DtoMapper dtoMapper;

    // ── Helper to get current user ID from JWT ──────────────────────

    private Long getCurrentUserId(Authentication authentication) {
        return (Long) authentication.getCredentials();
    }

    // ── Profile & Goals ──────────────────────────────────────────────

    /**
     * GET /api/user/profile — Get the current user's profile.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication auth) {
        User user = userService.getUserById(getCurrentUserId(auth));
        return ResponseEntity.ok(dtoMapper.toUserResponse(user));
    }

    /**
     * PUT /api/user/goals — UC2: Update daily calorie and protein goals.
     */
    @PutMapping("/goals")
    public ResponseEntity<?> updateGoals(Authentication auth,
                                         @Valid @RequestBody GoalUpdateRequest request) {
        try {
            User user = userService.updateDailyGoals(
                    getCurrentUserId(auth),
                    request.getDailyCalorieGoal(),
                    request.getDailyProteinGoal()
            );
            return ResponseEntity.ok(dtoMapper.toUserResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── Daily Log ────────────────────────────────────────────────────

    /**
     * GET /api/user/today — Get today's daily log summary (main dashboard data).
     */
    @GetMapping("/today")
    public ResponseEntity<DailyLogResponse> getTodaySummary(Authentication auth) {
        DailyLogService.DailyLogSummary summary = dailyLogService.getTodaySummary(getCurrentUserId(auth));
        return ResponseEntity.ok(dtoMapper.toDailyLogResponse(summary));
    }

    /**
     * GET /api/user/history — Get the user's daily log history.
     */
    @GetMapping("/history")
    public ResponseEntity<List<DailyLogResponse>> getHistory(Authentication auth) {
        Long userId = getCurrentUserId(auth);
        User user = userService.getUserById(userId);
        List<DailyLogResponse> history = dailyLogService.getUserLogHistory(userId).stream()
                .map(log -> dtoMapper.toDailyLogResponse(log, user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }

    // ── UC3: Add / Remove Consumed Meals ──────────────────────────────

    /**
     * POST /api/user/meals — Add a consumed meal to today's log.
     */
    @PostMapping("/meals")
    public ResponseEntity<?> addConsumedMeal(Authentication auth,
                                             @Valid @RequestBody ConsumedMealRequest request) {
        try {
            DailyLog updatedLog = dailyLogService.addConsumedMeal(
                    getCurrentUserId(auth),
                    request.getMealId(),
                    request.getQuantity()
            );
            DailyLogService.DailyLogSummary summary = dailyLogService.getTodaySummary(getCurrentUserId(auth));
            return ResponseEntity.ok(dtoMapper.toDailyLogResponse(summary));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/user/meals/{consumedMealId} — Remove a consumed meal entry.
     */
    @DeleteMapping("/meals/{consumedMealId}")
    public ResponseEntity<?> removeConsumedMeal(Authentication auth,
                                                @PathVariable Long consumedMealId) {
        try {
            dailyLogService.removeConsumedMeal(consumedMealId);
            DailyLogService.DailyLogSummary summary = dailyLogService.getTodaySummary(getCurrentUserId(auth));
            return ResponseEntity.ok(dtoMapper.toDailyLogResponse(summary));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── UC5: Add / Remove Performed Workouts ─────────────────────────

    /**
     * POST /api/user/workouts — Add a performed workout to today's log.
     */
    @PostMapping("/workouts")
    public ResponseEntity<?> addPerformedWorkout(Authentication auth,
                                                 @Valid @RequestBody PerformedWorkoutRequest request) {
        try {
            DailyLog updatedLog = dailyLogService.addPerformedWorkout(
                    getCurrentUserId(auth),
                    request.getWorkoutId(),
                    request.getDurationMinutes()
            );
            DailyLogService.DailyLogSummary summary = dailyLogService.getTodaySummary(getCurrentUserId(auth));
            return ResponseEntity.ok(dtoMapper.toDailyLogResponse(summary));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/user/workouts/{performedWorkoutId} — Remove a performed workout entry.
     */
    @DeleteMapping("/workouts/{performedWorkoutId}")
    public ResponseEntity<?> removePerformedWorkout(Authentication auth,
                                                    @PathVariable Long performedWorkoutId) {
        try {
            dailyLogService.removePerformedWorkout(performedWorkoutId);
            DailyLogService.DailyLogSummary summary = dailyLogService.getTodaySummary(getCurrentUserId(auth));
            return ResponseEntity.ok(dtoMapper.toDailyLogResponse(summary));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
