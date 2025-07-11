package com.healthconnect.repository;

import com.healthconnect.entity.Mood;
import com.healthconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Long> {
	List<Mood> findByUser(User user);

	List<Mood> findByUserAndMoodDateBetween(User user, LocalDate startDate, LocalDate endDate);

	@Query("SELECT AVG(m.moodRating) FROM Mood m WHERE m.user = :user AND m.moodDate BETWEEN :start AND :end")
	Double getAverageMoodRating(User user, LocalDate start, LocalDate end);

	List<Mood> findByUserAndMoodDate(User user, LocalDate moodDate);

	List<Mood> findByUserAndMoodRatingGreaterThanEqual(User user, Integer moodRating);
}