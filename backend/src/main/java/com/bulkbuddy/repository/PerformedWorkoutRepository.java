package com.bulkbuddy.repository;

import com.bulkbuddy.domain.entity.PerformedWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link PerformedWorkout} entities.
 */
@Repository
public interface PerformedWorkoutRepository extends JpaRepository<PerformedWorkout, Long> {

    /**
     * Find all performed workouts for a specific daily log.
     *
     * @param dailyLogId the daily log's ID
     * @return list of performed workout entries
     */
    List<PerformedWorkout> findByDailyLogId(Long dailyLogId);
}
