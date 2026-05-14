package com.bulkbuddy.repository;

import com.bulkbuddy.domain.entity.DailyLog;
import com.bulkbuddy.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link DailyLog} entities.
 */
@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    /**
     * Find the daily log for a specific user on a specific date.
     * Each (user, date) pair should be unique.
     *
     * @param user the user
     * @param date the date
     * @return an Optional containing the daily log, or empty if none exists yet
     */
    Optional<DailyLog> findByUserAndDate(User user, LocalDate date);

    /**
     * Find the daily log for a specific user ID on a specific date.
     *
     * @param userId the user's ID
     * @param date   the date
     * @return an Optional containing the daily log
     */
    Optional<DailyLog> findByUserIdAndDate(Long userId, LocalDate date);

    /**
     * Find all daily logs for a specific user, ordered by date descending.
     *
     * @param user the user
     * @return list of daily logs, most recent first
     */
    List<DailyLog> findByUserOrderByDateDesc(User user);

    /**
     * Find all daily logs for a specific user within a date range.
     *
     * @param user      the user
     * @param startDate start of the range (inclusive)
     * @param endDate   end of the range (inclusive)
     * @return list of daily logs within the range
     */
    List<DailyLog> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
