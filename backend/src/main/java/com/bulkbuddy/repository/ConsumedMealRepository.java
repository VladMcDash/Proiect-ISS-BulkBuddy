package com.bulkbuddy.repository;

import com.bulkbuddy.domain.entity.ConsumedMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link ConsumedMeal} entities.
 */
@Repository
public interface ConsumedMealRepository extends JpaRepository<ConsumedMeal, Long> {

    /**
     * Find all consumed meals for a specific daily log.
     *
     * @param dailyLogId the daily log's ID
     * @return list of consumed meal entries
     */
    List<ConsumedMeal> findByDailyLogId(Long dailyLogId);
}
