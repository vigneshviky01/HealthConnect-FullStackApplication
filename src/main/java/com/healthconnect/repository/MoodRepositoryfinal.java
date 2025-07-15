package com.healthconnect.repository;

// JPA repository for Mood entity
import com.healthconnect.entity.Mood;
import com.healthconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

// Extends JpaRepository for CRUD operations on Mood
public interface MoodRepository extends JpaRepository<Mood, Long> {

    // Finds all mood records for a given user
    List<Mood> findByUser(User user);

    // Finds mood records for a user within a date range
    List<Mood> findByUserAndMoodDateBetween(User user, LocalDate startDate, LocalDate endDate);

    // Calculates average mood rating for a user within a date range
    @Query("SELECT AVG(m.moodRating) FROM Mood m WHERE m.user = :user AND m.moodDate BETWEEN :start AND :end")
    Double getAverageMoodRating(User user, LocalDate start, LocalDate end);
}
