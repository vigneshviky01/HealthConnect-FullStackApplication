package com.healthconnect.transfer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response class for weekly aggregated health metrics.
 * Fields may be null if filtered out or if no data is present for the period.
 */
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
    private String startDate;
    private String endDate;
}