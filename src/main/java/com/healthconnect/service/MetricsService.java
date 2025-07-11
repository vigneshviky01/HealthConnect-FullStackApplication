package com.healthconnect.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthconnect.entity.User;
import com.healthconnect.repository.ActivityRepository;
import com.healthconnect.repository.MoodRepository;
import com.healthconnect.repository.SleepRepository;
import com.healthconnect.repository.WaterIntakeRepository;
import com.healthconnect.transfer.response.MetricsResponse;

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


    // Aggregates metrics for exactly 4 weeks (28 days) for a given month (1st to 28th).
    public List<MetricsResponse> getFourWeekMetricsForMonth(User user, YearMonth yearMonth, Set<String> metricsFilter) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atDay(28);
        return getFourWeekMetrics(user, start, end, metricsFilter);
    }

    // Aggregates metrics for exactly 4 weeks (28 days) from today.
    public List<MetricsResponse> getFourWeekMetricsFromToday(User user, Set<String> metricsFilter) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(27);
        return getFourWeekMetrics(user, start, end, metricsFilter);
    }

    // Helper to aggregate metrics for exactly 4 weeks (28 days) from start to end.
    private List<MetricsResponse> getFourWeekMetrics(User user, LocalDate start, LocalDate end, Set<String> metricsFilter) {
        List<MetricsResponse> weeklyMetrics = new ArrayList<>();
        LocalDate weekStart = start;
        for (int i = 0; i < 4; i++) {
            LocalDate weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(end)) weekEnd = end;
            
            // Aggregate activity
            Long totalSteps = metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("totalSteps") ?
                activityRepository.getTotalSteps(user, weekStart, weekEnd) : null;
            Long totalCalories = metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("totalCalories") ?
                activityRepository.getTotalCalories(user, weekStart, weekEnd) : null;
            Double totalDistance = metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("totalDistance") ?
                activityRepository.getTotalDistance(user, weekStart, weekEnd) : null;
            Integer totalWorkoutMinutes = metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("totalWorkoutMinutes") ?
                activityRepository.getTotalWorkoutMinutes(user, weekStart, weekEnd) : null;
            
            // Aggregate sleep records
            Double totalSleepHours = null;
            Double avgSleepQuality = null;
            if (metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("totalSleepHours") || metricsFilter.contains("avgSleepQuality")) {
                LocalDateTime startDateTime = weekStart.atStartOfDay();
                LocalDateTime endDateTime = weekEnd.plusDays(1).atStartOfDay().minusSeconds(1);
                if (metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("totalSleepHours")) {
                    List<com.healthconnect.entity.Sleep> sleeps = sleepRepository.findByUserAndSleepStartTimeBetween(user, startDateTime, endDateTime);
                    double sumHours = 0.0;
                    for (com.healthconnect.entity.Sleep s : sleeps) {
                        if (s.getSleepStartTime() != null && s.getSleepEndTime() != null) {
                            sumHours += java.time.Duration.between(s.getSleepStartTime(), s.getSleepEndTime()).toMinutes() / 60.0;
                        }
                    }
                    totalSleepHours = sleeps.isEmpty() ? 0.0 : sumHours;
                }
                if (metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("avgSleepQuality")) {
                    avgSleepQuality = sleepRepository.getAvgSleepQuality(user.getId(), startDateTime, endDateTime);
                }
            }
            
            // Aggregate water intake records
            Double totalWaterIntake = metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("totalWaterIntake") ?
                waterIntakeRepository.getTotalWaterIntake(user, weekStart, weekEnd) : null;
            
            // Aggregate mood
            Double avgMoodRating = metricsFilter == null || metricsFilter.isEmpty() || metricsFilter.contains("avgMoodRating") ?
                moodRepository.getAverageMoodRating(user, weekStart, weekEnd) : null;
            
            // Build response, omitting nulls for filtered metrics
            MetricsResponse.MetricsResponseBuilder builder = MetricsResponse.builder();
            if (totalSteps != null) builder.totalSteps(totalSteps);
            if (totalCalories != null) builder.totalCalories(totalCalories);
            if (totalDistance != null) builder.totalDistance(totalDistance);
            if (totalWorkoutMinutes != null) builder.totalWorkoutMinutes(totalWorkoutMinutes);
            if (totalSleepHours != null) builder.totalSleepHours(totalSleepHours);
            if (avgSleepQuality != null) builder.avgSleepQuality(avgSleepQuality);
            if (totalWaterIntake != null) builder.totalWaterIntake(totalWaterIntake);
            if (avgMoodRating != null) builder.avgMoodRating(avgMoodRating);
            builder.startDate(weekStart.toString());
            builder.endDate(weekEnd.toString());
            weeklyMetrics.add(builder.build());
            weekStart = weekEnd.plusDays(1);
        }
        
        return weeklyMetrics;
    }
}