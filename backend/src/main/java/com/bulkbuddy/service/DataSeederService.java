package com.bulkbuddy.service;

import com.bulkbuddy.domain.entity.Meal;
import com.bulkbuddy.domain.entity.Workout;
import com.bulkbuddy.domain.enums.Role;
import com.bulkbuddy.repository.MealRepository;
import com.bulkbuddy.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Populates the database with standardized Meals and Workouts on startup.
 * <p>
 * Acts as a "CSV import" using hardcoded data — simulates reading from a CSV file.
 * Only inserts data if the respective tables are empty (idempotent).
 * Also creates default Admin and User accounts for development.
 * </p>
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DataSeederService implements CommandLineRunner {

    private final MealRepository mealRepository;
    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    @Override
    public void run(String... args) {
        seedMeals();
        seedWorkouts();
        seedUsers();
        log.info("✅ Data seeding complete.");
    }

    // ── Meals (simulating CSV: name, caloriesPer100g, proteinPer100g, imageUrl) ──

    private void seedMeals() {
        if (mealRepository.count() > 0) {
            log.info("Meals table already populated — skipping seed.");
            return;
        }

        // CSV-like data: name | calories per 100g | protein per 100g | image URL
        List<String[]> mealsCsv = List.of(
            // Proteins
            new String[]{"Grilled Chicken Breast",  "165", "31", "https://images.unsplash.com/photo-1532550907401-a500c9a57435?w=400"},
            new String[]{"Salmon Fillet",            "208", "20", "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=400"},
            new String[]{"Lean Ground Beef",         "250", "26", "https://images.unsplash.com/photo-1588168333986-5078d3ae3976?w=400"},
            new String[]{"Eggs (Whole)",             "155", "13", "https://images.unsplash.com/photo-1482049016688-2d3e1b311543?w=400"},
            new String[]{"Greek Yogurt",             "59",  "10", "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400"},
            new String[]{"Cottage Cheese",           "98",  "11", "https://images.unsplash.com/photo-1559181567-c3190ca9959b?w=400"},
            new String[]{"Turkey Breast",            "135", "30", "https://images.unsplash.com/photo-1574672280600-4accfa404237?w=400"},
            new String[]{"Tuna (Canned)",            "132", "29", "https://images.unsplash.com/photo-1534604973900-c43ab4c2e0ab?w=400"},
            new String[]{"Shrimp",                   "99",  "24", "https://images.unsplash.com/photo-1565680018434-b513d5e5fd47?w=400"},
            new String[]{"Tofu",                     "76",  "8",  "https://images.unsplash.com/photo-1628689469838-524a4a973b8e?w=400"},

            // Carbs
            new String[]{"White Rice (Cooked)",      "130", "3",  "https://images.unsplash.com/photo-1516684732162-798a0062be99?w=400"},
            new String[]{"Brown Rice (Cooked)",      "112", "3",  "https://images.unsplash.com/photo-1536304993881-ff6e9eefa2a6?w=400"},
            new String[]{"Pasta (Cooked)",           "131", "5",  "https://images.unsplash.com/photo-1551462147-ff29053bfc14?w=400"},
            new String[]{"Sweet Potato",             "86",  "2",  "https://images.unsplash.com/photo-1596097635121-14b63a7e0e75?w=400"},
            new String[]{"Oatmeal (Cooked)",         "68",  "2",  "https://images.unsplash.com/photo-1517673400267-0251440c45dc?w=400"},
            new String[]{"Whole Wheat Bread",        "247", "13", "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400"},
            new String[]{"Banana",                   "89",  "1",  "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400"},
            new String[]{"Apple",                    "52",  "0",  "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400"},

            // Fats & Others
            new String[]{"Avocado",                  "160", "2",  "https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=400"},
            new String[]{"Almonds",                  "579", "21", "https://images.unsplash.com/photo-1508061253366-f7da158b6d46?w=400"},
            new String[]{"Peanut Butter",            "588", "25", "https://images.unsplash.com/photo-1643647858054-a7c6d6dedc11?w=400"},
            new String[]{"Olive Oil",                "884", "0",  "https://images.unsplash.com/photo-1474979266404-7eaacdc948b6?w=400"},
            new String[]{"Mixed Salad",              "20",  "2",  "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400"},
            new String[]{"Broccoli",                 "34",  "3",  "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=400"},
            new String[]{"Quinoa (Cooked)",          "120", "4",  "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400"},

            // Meals / Prepared
            new String[]{"Protein Shake",            "120", "25", "https://images.unsplash.com/photo-1622485831930-34ac18919e55?w=400"},
            new String[]{"Chicken & Rice Bowl",      "180", "18", "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=400"},
            new String[]{"Beef Stir Fry",            "155", "15", "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=400"},
            new String[]{"Caesar Salad",             "127", "8",  "https://images.unsplash.com/photo-1546793665-c74683f339c1?w=400"},
            new String[]{"Granola Bar",              "471", "10", "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400"}
        );

        for (String[] row : mealsCsv) {
            Meal meal = Meal.builder()
                    .name(row[0])
                    .calories(Integer.parseInt(row[1]))
                    .protein(Integer.parseInt(row[2]))
                    .imageUrl(row[3])
                    .build();
            mealRepository.save(meal);
        }

        log.info("Seeded {} standardized meals.", mealsCsv.size());
    }

    // ── Workouts (simulating CSV: type, caloriesBurnedPerHour) ───────

    private void seedWorkouts() {
        if (workoutRepository.count() > 0) {
            log.info("Workouts table already populated — skipping seed.");
            return;
        }

        // CSV-like data: type | calories burned per 1 hour
        List<String[]> workoutsCsv = List.of(
            new String[]{"Running (Moderate)",       "600"},
            new String[]{"Running (High Intensity)", "800"},
            new String[]{"Walking (Brisk)",          "300"},
            new String[]{"Cycling (Moderate)",       "500"},
            new String[]{"Cycling (High Intensity)", "700"},
            new String[]{"Swimming",                 "550"},
            new String[]{"Weight Training",          "400"},
            new String[]{"HIIT",                     "700"},
            new String[]{"CrossFit",                 "650"},
            new String[]{"Yoga",                     "200"},
            new String[]{"Pilates",                  "250"},
            new String[]{"Rowing",                   "600"},
            new String[]{"Jump Rope",                "750"},
            new String[]{"Elliptical",               "450"},
            new String[]{"Stair Climbing",           "500"},
            new String[]{"Boxing",                   "700"},
            new String[]{"Dancing",                  "350"},
            new String[]{"Hiking",                   "400"},
            new String[]{"Basketball",               "550"},
            new String[]{"Soccer",                   "500"}
        );

        for (String[] row : workoutsCsv) {
            Workout workout = Workout.builder()
                    .type(row[0])
                    .caloriesBurned(Integer.parseInt(row[1]))
                    .build();
            workoutRepository.save(workout);
        }

        log.info("Seeded {} standardized workouts.", workoutsCsv.size());
    }

    // ── Default Users ────────────────────────────────────────────────

    private void seedUsers() {
        // Create default admin
        if (userService.findByUsername("admin").isEmpty()) {
            userService.registerUser("admin", "admin123", Role.ADMIN, 2500, 150);
            log.info("Created default admin user.");
        }

        // Create default regular user
        if (userService.findByUsername("john").isEmpty()) {
            userService.registerUser("john", "john123", Role.USER, 2200, 130);
            log.info("Created default user 'john'.");
        }

        // Create a second test user
        if (userService.findByUsername("jane").isEmpty()) {
            userService.registerUser("jane", "jane123", Role.USER, 1800, 100);
            log.info("Created default user 'jane'.");
        }
    }
}
