package com.healthconnect.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthconnect.entity.Sleep;
import com.healthconnect.entity.User;

public interface SleepRepository extends JpaRepository<Sleep, Long>{
	List<Sleep> findByUser(User user);
	List<Sleep> findByUserAndSleepStartTimeBetween(User user, LocalDateTime start, LocalDateTime end);
	List<Sleep> findByUserAndQualityRatingGreaterThanEqual(User user, Integer qualityRating);
	List<Sleep> findByUserAndSleepStartTimeBetweenAndQualityRatingGreaterThanEqual(User user, LocalDateTime start, LocalDateTime end, Integer qualityRating);
	
	// Find sleep records for a specific date (ignoring time)
	@Query("SELECT s FROM Sleep s WHERE s.user = :user AND CAST(s.sleepStartTime AS LocalDate) = :date")
	List<Sleep> findByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
}
