package com.bulkbuddy.service;

import com.bulkbuddy.domain.entity.User;
import com.bulkbuddy.domain.enums.Role;
import com.bulkbuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for {@link User} management.
 * Handles user registration, lookup, and goal updates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user with encoded password.
     *
     * @param username         desired username
     * @param rawPassword      plain-text password
     * @param role             ADMIN or USER
     * @param dailyCalorieGoal daily calorie target
     * @param dailyProteinGoal daily protein target (grams)
     * @return the persisted User
     * @throws IllegalArgumentException if the username already exists
     */
    public User registerUser(String username, String rawPassword, Role role,
                             int dailyCalorieGoal, int dailyProteinGoal) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .dailyCalorieGoal(dailyCalorieGoal)
                .dailyProteinGoal(dailyProteinGoal)
                .build();

        User saved = userRepository.save(user);
        log.info("Registered new {} user: {}", role, username);
        return saved;
    }

    /**
     * Find a user by ID.
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    /**
     * Find a user by username.
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get all users.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all users with a specific role.
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * UC2: User sets/updates their daily calorie and protein goals.
     *
     * @param userId           the user's ID
     * @param dailyCalorieGoal new calorie goal
     * @param dailyProteinGoal new protein goal (grams)
     * @return the updated user
     */
    public User updateDailyGoals(Long userId, int dailyCalorieGoal, int dailyProteinGoal) {
        User user = getUserById(userId);
        user.setDailyCalorieGoal(dailyCalorieGoal);
        user.setDailyProteinGoal(dailyProteinGoal);
        log.info("Updated goals for user {}: calories={}, protein={}",
                user.getUsername(), dailyCalorieGoal, dailyProteinGoal);
        return userRepository.save(user);
    }
}
