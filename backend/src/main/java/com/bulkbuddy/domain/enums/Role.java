package com.bulkbuddy.domain.enums;

/**
 * Defines the roles available within the BulkBuddy application.
 * <ul>
 *   <li>{@code ADMIN} — Can manage standardized Meals and Workouts (CRUD).</li>
 *   <li>{@code USER}  — Can log daily meals, workouts, and track macros.</li>
 * </ul>
 */
public enum Role {
    ADMIN,
    USER
}
