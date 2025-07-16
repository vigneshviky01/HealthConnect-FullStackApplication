package com.healthconnect.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.healthconnect.entity.Activity;
import com.healthconnect.entity.User;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

	List<Activity> findByUser(User user);

	List<Activity> findByUserAndActivityDate(User user, LocalDate date);

	List<Activity> findByUserAndActivityDateBetween(User user, LocalDate startDate, LocalDate endDate);

	// Aggregated Data

	@Query("SELECT SUM(a.stepsCount) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Long getTotalSteps(User user, LocalDate start, LocalDate end);

	@Query("SELECT SUM(a.caloriesBurned) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Long getTotalCalories(User user, LocalDate start, LocalDate end);

	@Query("SELECT SUM(a.distanceKm) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Double getTotalDistance(User user, LocalDate start, LocalDate end);

	@Query("SELECT SUM(a.workoutDurationMinutes) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Integer getTotalWorkoutMinutes(User user, LocalDate start, LocalDate end);
}