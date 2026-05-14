import api from './api';
import type { Notification } from '../types';

export const notificationService = {
  getAll: async (): Promise<Notification[]> => {
    const { data } = await api.get<Notification[]>('/user/notifications');
    return data;
  },

  getUnread: async (): Promise<Notification[]> => {
    const { data } = await api.get<Notification[]>('/user/notifications/unread');
    return data;
  },

  getUnreadCount: async (): Promise<number> => {
    const { data } = await api.get<{ unreadCount: number }>('/user/notifications/count');
    return data.unreadCount;
  },

  markAsRead: async (id: number): Promise<Notification> => {
    const { data } = await api.patch<Notification>(`/user/notifications/${id}/read`);
    return data;
  },

  markAllAsRead: async (): Promise<void> => {
    await api.post('/user/notifications/read-all');
  },
};
