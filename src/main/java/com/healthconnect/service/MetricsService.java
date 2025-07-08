package com.healthconnect.service;

import com.healthconnect.entity.User;
import com.healthconnect.repository.ActivityRepository;
import com.healthconnect.repository.MoodRepository;
import com.healthconnect.repository.SleepRepository;
import com.healthconnect.repository.WaterIntakeRepository;
import com.healthconnect.transfer.response.MetricsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

@Service
public class MetricsService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SleepRepository sleepRepository;

    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private WaterIntakeRepository waterIntakeRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Get weekly metrics for a user
    public MetricsResponse getWeeklyMetrics(User user) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        
        return getMetricsForDateRange(user, startOfWeek, endOfWeek, "WEEK");
    }

    // Get monthly metrics for a user
    public MetricsResponse getMonthlyMetrics(User user) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        return getMetricsForDateRange(user, startOfMonth, endOfMonth, "MONTH");
    }

    // Get metrics for a custom date range
    public MetricsResponse getMetricsForDateRange(User user, LocalDate startDate, LocalDate endDate, String timeFrame) {
        // Convert LocalDate to LocalDateTime for sleep queries
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusSeconds(1);
        
        // Get aggregated metrics from repositories
        Long totalSteps = activityRepository.getTotalSteps(user, startDate, endDate);
        Long totalCalories = activityRepository.getTotalCalories(user, startDate, endDate);
        Double totalDistance = activityRepository.getTotalDistance(user, startDate, endDate);
        Integer totalWorkoutMinutes = activityRepository.getTotalWorkoutMinutes(user, startDate, endDate);
        
        Double avgSleepQuality = sleepRepository.getAvgSleepQuality(user.getId(), startDateTime, endDateTime);
        Double avgSleepHours = sleepRepository.getAvgSleepHours(user.getId(), startDateTime, endDateTime);
        
        Double totalWaterIntake = waterIntakeRepository.getTotalWaterIntake(user, startDate, endDate);
        
        Double avgMoodRating = moodRepository.getAverageMoodRating(user, startDate, endDate);
        
        // Build and return the response
        return MetricsResponse.builder()
                .totalSteps(totalSteps != null ? totalSteps : 0L)
                .totalCalories(totalCalories != null ? totalCalories : 0L)
                .totalDistance(totalDistance != null ? totalDistance : 0.0)
                .totalWorkoutMinutes(totalWorkoutMinutes != null ? totalWorkoutMinutes : 0)
                .totalSleepHours(avgSleepHours != null ? avgSleepHours : 0.0)
                .avgSleepQuality(avgSleepQuality != null ? avgSleepQuality : 0.0)
                .totalWaterIntake(totalWaterIntake != null ? totalWaterIntake : 0.0)
                .avgMoodRating(avgMoodRating != null ? avgMoodRating : 0.0)
                .timeFrame(timeFrame)
                .startDate(startDate.format(DATE_FORMATTER))
                .endDate(endDate.format(DATE_FORMATTER))
                .build();
    }
}