import api from './api';
import type { Meal } from '../types';

export const mealService = {
  // Shared (Auth)
  searchMeals: async (query?: string): Promise<Meal[]> => {
    const { data } = await api.get<Meal[]>('/meals', { params: { q: query } });
    return data;
  },

  // Admin Only
  getAllMeals: async (): Promise<Meal[]> => {
    const { data } = await api.get<Meal[]>('/admin/meals');
    return data;
  },

  createMeal: async (meal: Omit<Meal, 'id'>): Promise<Meal> => {
    const { data } = await api.post<Meal>('/admin/meals', meal);
    return data;
  },

  updateMeal: async (id: number, meal: Omit<Meal, 'id'>): Promise<Meal> => {
    const { data } = await api.put<Meal>(`/admin/meals/${id}`, meal);
    return data;
  },

  deleteMeal: async (id: number): Promise<void> => {
    await api.delete(`/admin/meals/${id}`);
  },
};
