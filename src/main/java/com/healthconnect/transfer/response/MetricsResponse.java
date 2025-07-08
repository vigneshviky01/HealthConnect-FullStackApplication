package com.healthconnect.transfer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Response class for aggregated health metrics data
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsResponse {
    private Long totalSteps;
    private Long totalCalories;
    private Double totalDistance;
    private Integer totalWorkoutMinutes;
    private Double totalSleepHours;
    private Double avgSleepQuality;
    private Double totalWaterIntake;
    private Double avgMoodRating;
    private String timeFrame; // "WEEK" or "MONTH"
    private String startDate;
    private String endDate;
}