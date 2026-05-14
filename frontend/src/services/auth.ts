import api from './api';
import type { AuthResponse, User } from '../types';

export const authService = {
  login: async (username: string, password: string): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>('/auth/login', { username, password });
    return data;
  },

  selectUser: async (userId: number): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>(`/auth/select/${userId}`);
    return data;
  },

  getAvailableUsers: async (): Promise<User[]> => {
    const { data } = await api.get<User[]>('/auth/users');
    return data;
  },
};
