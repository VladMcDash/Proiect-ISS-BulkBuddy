package com.bulkbuddy.repository;

import com.bulkbuddy.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Notification} entities.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find all notifications for a specific user, ordered by date descending.
     *
     * @param userId the user's ID
     * @return list of notifications, most recent first
     */
    List<Notification> findByUserIdOrderByDateDesc(Long userId);

    /**
     * Find all unread notifications for a specific user.
     *
     * @param userId the user's ID
     * @return list of unread notifications
     */
    List<Notification> findByUserIdAndIsReadFalse(Long userId);

    /**
     * Count unread notifications for a specific user.
     *
     * @param userId the user's ID
     * @return number of unread notifications
     */
    long countByUserIdAndIsReadFalse(Long userId);
}
