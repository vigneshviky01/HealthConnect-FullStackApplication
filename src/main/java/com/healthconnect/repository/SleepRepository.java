package com.healthconnect.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthconnect.entity.Sleep;
import com.healthconnect.entity.User;

public interface SleepRepository extends JpaRepository<Sleep, Long>{
	List<Sleep> findByUser(User user);
	List<Sleep> findByUserAndSleepStartTimeBetween(User user, LocalDateTime start, LocalDateTime end);
}
