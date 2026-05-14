import React from 'react';
import type { Notification } from '../types';
import { CheckCircle, X } from 'lucide-react';
import { clsx } from 'clsx';

interface NotificationAlertProps {
  notification: Notification;
  onMarkAsRead: (id: number) => void;
}

const NotificationAlert: React.FC<NotificationAlertProps> = ({ notification, onMarkAsRead }) => {
  if (notification.isRead) return null;

  const isSuccess = notification.message.includes('🎉') || notification.message.includes('🔥') || notification.message.includes('💪');

  return (
    <div className={clsx(
      "flex items-start justify-between p-4 rounded-xl border mb-3 shadow-sm transition-all animate-in slide-in-from-right-8 duration-300",
      isSuccess ? "bg-green-50 border-green-200" : "bg-blue-50 border-blue-200"
    )}>
      <div className="flex items-start space-x-3">
        <div className={clsx(
          "mt-0.5 p-1 rounded-full",
          isSuccess ? "text-green-600 bg-green-100" : "text-blue-600 bg-blue-100"
        )}>
          <CheckCircle className="w-5 h-5" />
        </div>
        <div>
          <p className={clsx("text-sm font-medium", isSuccess ? "text-green-800" : "text-blue-800")}>
            {notification.message}
          </p>
          <p className={clsx("text-xs mt-1", isSuccess ? "text-green-600/80" : "text-blue-600/80")}>
            {new Date(notification.date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
          </p>
        </div>
      </div>
      <button 
        onClick={() => onMarkAsRead(notification.id)}
        className={clsx(
          "p-1.5 rounded-lg transition-colors",
          isSuccess ? "text-green-600 hover:bg-green-100" : "text-blue-600 hover:bg-blue-100"
        )}
      >
        <X className="w-4 h-4" />
      </button>
    </div>
  );
};

export default NotificationAlert;
