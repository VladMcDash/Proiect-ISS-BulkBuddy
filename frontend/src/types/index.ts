export enum Role {
  ADMIN = 'ADMIN',
  USER = 'USER',
}

export interface User {
  id: number;
  username: string;
  role: Role;
  dailyCalorieGoal: number;
  dailyProteinGoal: number;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: number;
  username: string;
  role: Role;
}

export interface Meal {
  id: number;
  name: string;
  calories: number;
  protein: number;
  imageUrl?: string;
}

export interface Workout {
  id: number;
  type: string;
  caloriesBurned: number;
}

export interface ConsumedMealEntry {
  id: number;
  mealId: number;
  mealName: string;
  mealImageUrl?: string;
  caloriesPer100g: number;
  proteinPer100g: number;
  quantity: number;
  totalCalories: number;
  totalProtein: number;
}

export interface PerformedWorkoutEntry {
  id: number;
  workoutId: number;
  workoutType: string;
  caloriesBurnedPerHour: number;
  durationMinutes: number;
  totalCaloriesBurned: number;
}

export interface DailyLog {
  id: number;
  date: string;
  calorieGoal: number;
  proteinGoal: number;
  caloriesConsumed: number;
  caloriesBurned: number;
  proteinConsumed: number;
  remainingCalories: number;
  remainingProtein: number;
  goalsComplete: boolean;
  consumedMeals: ConsumedMealEntry[];
  performedWorkouts: PerformedWorkoutEntry[];
}

export interface Notification {
  id: number;
  message: string;
  isRead: boolean;
  date: string;
}

export interface GoalUpdateRequest {
  dailyCalorieGoal: number;
  dailyProteinGoal: number;
}
