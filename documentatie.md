Act as an Expert Full-Stack Developer (Java Spring Boot + React Node.js TypeScript). 
Your task is to generate a fully working, production-ready application called "BulkBuddy" (a Gym Tracker: Meal & Health Tracker).

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

### 5. EXECUTION PLAN (IMPORTANT)
To avoid output limits, please generate the application in **PHASES**. 
Right now, ONLY execute **PHASE 1**. When I reply with "Continue", execute the next phase.

* **PHASE 1 (Backend Domain & Repositories):** Write the Spring Boot configurations (`application.properties` for H2), the Entities (`User`, `Meal`, `Workout`, `DailyLog`, `ConsumedMeal`, `PerformedWorkout`, `Notification`), and their Spring Data JPA Repositories. Include the Enum for `Role`.
* **PHASE 2 (Backend Services & CSV Initialization):** Write the Service layer containing the business logic (calorie calculations, goal checking, notification generation) and the `CommandLineRunner` or `@PostConstruct` service that reads a mock CSV (or hardcoded list acting as CSV) to populate standard Meals and Workouts if the DB is empty.
* **PHASE 3 (Backend Controllers & Security):** Write the REST API Controllers (for Admin and User routes) and a basic Spring Security configuration using JWT.
* **PHASE 4 (Frontend Setup & Types):** Write the React TypeScript Interfaces (matching the backend entities), Axios API service files, and the Authentication Context/State.
* **PHASE 5 (Frontend UI):** Write the React Components: Login Page, Admin Dashboard (CRUD Meals/Workouts), and User Dashboard (Daily Log, Macro Progress bars, Notification alerts). Use Tailwind CSS classes for styling.

Please start with PHASE 1 now. Provide fully written, production-ready files, without omitting imports.