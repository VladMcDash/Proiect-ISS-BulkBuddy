import api from './api';
import type { DailyLog, GoalUpdateRequest, User } from '../types';

export const dashboardService = {
  // Profile & Goals
  getProfile: async (): Promise<User> => {
    const { data } = await api.get<User>('/user/profile');
    return data;
  },

  updateGoals: async (goals: GoalUpdateRequest): Promise<User> => {
    const { data } = await api.put<User>('/user/goals', goals);
    return data;
  },

  // Daily Log
  getTodaySummary: async (): Promise<DailyLog> => {
    const { data } = await api.get<DailyLog>('/user/today');
    return data;
  },

  getHistory: async (): Promise<DailyLog[]> => {
    const { data } = await api.get<DailyLog[]>('/user/history');
    return data;
  },

  // Meals Tracking
  addConsumedMeal: async (mealId: number, quantity: number): Promise<DailyLog> => {
    const { data } = await api.post<DailyLog>('/user/meals', { mealId, quantity });
    return data;
  },

  removeConsumedMeal: async (consumedMealId: number): Promise<DailyLog> => {
    const { data } = await api.delete<DailyLog>(`/user/meals/${consumedMealId}`);
    return data;
  },

  // Workouts Tracking
  addPerformedWorkout: async (workoutId: number, durationMinutes: number): Promise<DailyLog> => {
    const { data } = await api.post<DailyLog>('/user/workouts', { workoutId, durationMinutes });
    return data;
  },

  removePerformedWorkout: async (performedWorkoutId: number): Promise<DailyLog> => {
    const { data } = await api.delete<DailyLog>(`/user/workouts/${performedWorkoutId}`);
    return data;
  },
};
