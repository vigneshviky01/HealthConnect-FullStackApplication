package com.healthconnect.repository;

import com.healthconnect.entity.Mood;
import com.healthconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    List<Mood> findByUser(User user);
    List<Mood> findByUserAndMoodDateBetween(User user, LocalDate startDate, LocalDate endDate);
}