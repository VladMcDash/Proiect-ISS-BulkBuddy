import api from './api';
import type { Workout } from '../types';

export const workoutService = {
  // Shared (Auth)
  searchWorkouts: async (query?: string): Promise<Workout[]> => {
    const { data } = await api.get<Workout[]>('/workouts', { params: { q: query } });
    return data;
  },

  // Admin Only
  getAllWorkouts: async (): Promise<Workout[]> => {
    const { data } = await api.get<Workout[]>('/admin/workouts');
    return data;
  },

  createWorkout: async (workout: Omit<Workout, 'id'>): Promise<Workout> => {
    const { data } = await api.post<Workout>('/admin/workouts', workout);
    return data;
  },

  updateWorkout: async (id: number, workout: Omit<Workout, 'id'>): Promise<Workout> => {
    const { data } = await api.put<Workout>(`/admin/workouts/${id}`, workout);
    return data;
  },

  deleteWorkout: async (id: number): Promise<void> => {
    await api.delete(`/admin/workouts/${id}`);
  },
};
