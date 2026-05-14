### 1. TECH STACK
- Backend: Java 17+, Spring Boot 3, Spring Data JPA, Spring Security (JWT authentication), PostgreSQL or H2 (in-memory for dev).
- Frontend: React 18, TypeScript, Node.js (npm), Tailwind CSS (for quick styling), Axios (for API calls).

### 2. CORE REQUIREMENTS & BUSINESS LOGIC
1. Users have a daily Calorie Goal and a daily Protein Goal.
2. The system tracks "Remaining Calories" (Goal - Consumed + Burned) and "Protein Intake" per day.
3. Admin users manage a standard Database of Meals and Workouts (CRUD). Users CANNOT create custom meals/workouts, they only select from the Admin's standardized DB.
4. Each daily login creates/fetches a `DailyLog` for that user for the current date.
5. Users log `ConsumedMeal` (linked to a standardized Meal + quantity) and `PerformedWorkout` (linked to a standardized Workout + quantity/duration).
6. The system calculates totals automatically.
7. System generates a `Notification` when the user successfully reaches their daily protein or calorie goals.
8. Auto-populate the database with standard Meals and Workouts from a CSV file on startup.

### 3. DOMAIN MODEL (UML Specifications)
Implement the following entities with JPA annotations. 

1. `User`
- id: Long
- username: String (Unique)
- password: String (Encrypted)
- role: Enum (ADMIN, USER) -> Note: Must be an Enum.
- dailyCalorieGoal: Integer
- dailyProteinGoal: Integer
- Method: getRemainingCalories(consumed, burned)

2. `Meal` (Standardized template)
- id: Long
- name: String
- calories: Integer
- protein: Integer

3. `Workout` (Standardized template)
- id: Long
- type: String
- caloriesBurned: Integer (per 1 unit of quantity/duration)

4. `DailyLog`
- id: Long
- user: ManyToOne
- date: LocalDate
- totalCaloriesConsumed: Integer
- totalProteinConsumed: Integer
- totalCaloriesBurned: Integer
- Method: checkGoals() : Boolean

5. `ConsumedMeal`
- id: Long
- dailyLog: ManyToOne
- meal: ManyToOne
- quantity: Integer

6. `PerformedWorkout`
- id: Long
- dailyLog: ManyToOne
- workout: ManyToOne
- quantity: Integer -> Note: Acts as a multiplier for caloriesBurned.

7. `Notification`
- id: Long
- user: ManyToOne
- message: String
- isRead: Boolean
- date: LocalDateTime

### 4. USE CASES TO IMPLEMENT
- UC1: Admin adds meals to DB.
- UC2: User sets/updates daily goals (calories + protein).
- UC3: User selects a meal they ate (adds to DailyLog, updates macro tracking).
- UC4: Admin adds workouts to DB.
- UC5: User selects a workout they did today (updates burned calories).
- UC6: Notify user upon completing calorie/protein goals.
