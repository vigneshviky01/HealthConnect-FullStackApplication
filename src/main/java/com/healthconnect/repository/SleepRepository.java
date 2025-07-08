package com.healthconnect.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthconnect.entity.Sleep;
import com.healthconnect.entity.User;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
	List<Sleep> findByUser(User user);

	List<Sleep> findByUserAndSleepStartTimeBetween(User user, LocalDateTime start, LocalDateTime end);

	List<Sleep> findByUserAndQualityRatingGreaterThanEqual(User user, Integer qualityRating);

	List<Sleep> findByUserAndSleepStartTimeBetweenAndQualityRatingGreaterThanEqual(User user, LocalDateTime start,
			LocalDateTime end, Integer qualityRating);

	// Find sleep records for a specific date (ignoring time)
	@Query("SELECT s FROM Sleep s WHERE s.user = :user AND CAST(s.sleepStartTime AS LocalDate) = :date")
	List<Sleep> findByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

	@Query(value = "SELECT AVG(TIMESTAMPDIFF(MINUTE, s.sleep_start, s.sleep_end) / 60.0) "
			+ "FROM sleep s WHERE s.user_id = :userId "
			+ "AND s.sleep_start BETWEEN :start AND :end", nativeQuery = true)
	Double getAvgSleepHours(@Param("userId") Long userId, @Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	@Query("SELECT AVG(s.qualityRating) FROM Sleep s WHERE s.user.id = :userId AND s.sleepStartTime BETWEEN :start AND :end")
	Double getAvgSleepQuality(@Param("userId") Long userId,
	                          @Param("start") LocalDateTime start,
	                          @Param("end") LocalDateTime end);

}
