package com.bulkbuddy.service;

import com.bulkbuddy.domain.entity.Notification;
import com.bulkbuddy.domain.entity.User;
import com.bulkbuddy.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for {@link Notification} management.
 * Handles creation of goal-completion notifications and read-state management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Create and persist a notification for a user.
     *
     * @param user    the target user
     * @param message the notification message
     * @return the persisted Notification
     */
    public Notification createNotification(User user, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .isRead(false)
                .date(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created for user {}: {}", user.getUsername(), message);
        return saved;
    }

    /**
     * Get all notifications for a user, most recent first.
     */
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByDateDesc(userId);
    }

    /**
     * Get all unread notifications for a user.
     */
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    /**
     * Count unread notifications for a user.
     */
    @Transactional(readOnly = true)
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    /**
     * Mark a single notification as read.
     */
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found with ID: " + notificationId));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    /**
     * Mark all notifications for a user as read.
     */
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
        log.info("Marked {} notifications as read for user ID {}", unread.size(), userId);
    }
}
