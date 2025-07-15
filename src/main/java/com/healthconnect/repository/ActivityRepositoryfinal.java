package com.healthconnect.repository;

// JPA repository for Activity entity
import com.healthconnect.entity.Activity;
import com.healthconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

// Extends JpaRepository for CRUD operations on Activity
public interface ActivityRepository extends JpaRepository<Activity, Long> {

	// Finds all activities for a given user
	List<Activity> findByUser(User user);

	// Finds activities for a user by workout type
	List<Activity> findByUserAndWorkoutType(User user, String workoutType);

	// Finds activities for a user within a date range
	List<Activity> findByUserAndActivityDateBetween(User user, LocalDate startDate, LocalDate endDate);

	// Finds activities for a user within a date range and workout type
	List<Activity> findByUserAndActivityDateBetweenAndWorkoutType(User user, LocalDate startDate, LocalDate endDate, String workoutType);

	// Finds activities for a user on a specific date
	List<Activity> findByUserAndActivityDate(User user, LocalDate date);

	// Aggregated Data Queries

	// Calculates total steps for a user within a date range
	@Query("SELECT SUM(a.stepsCount) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Long getTotalSteps(User user, LocalDate start, LocalDate end);

	// Calculates total calories burned for a user within a date range
	@Query("SELECT SUM(a.caloriesBurned) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Long getTotalCalories(User user, LocalDate start, LocalDate end);

	// Calculates total distance covered for a user within a date range
	@Query("SELECT SUM(a.distanceKm) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Double getTotalDistance(User user, LocalDate start, LocalDate end);

	// Calculates total workout minutes for a user within a date range
	@Query("SELECT SUM(a.workoutDurationMinutes) FROM Activity a WHERE a.user = :user AND a.activityDate BETWEEN :start AND :end")
	Integer getTotalWorkoutMinutes(User user, LocalDate start, LocalDate end);
}
