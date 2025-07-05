package com.healthconnect.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthconnect.entity.Activity;
import com.healthconnect.entity.User;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

	List<Activity> findByUser(User user);
	List<Activity> findByUserAndWorkoutType(User user, String workoutType);
	List<Activity> findByUserAndActivityDateBetween(User user, LocalDate startDate, LocalDate endDate);
	List<Activity> findByUserAndActivityDateBetweenAndWorkoutType(User user, LocalDate startDate, LocalDate endDate, String workoutType);
	List<Activity> findByUserAndActivityDate(User user, LocalDate date);
}