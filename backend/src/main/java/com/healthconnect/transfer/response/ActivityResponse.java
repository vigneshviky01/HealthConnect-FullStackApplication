package com.healthconnect.transfer.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.healthconnect.entity.Activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private Long id;
    private LocalDate activityDate;
    private Integer stepsCount;
    private Integer caloriesBurned;
    private String workoutType;
    private Integer workoutDurationMinutes;
    private Double distanceKm;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static ActivityResponse fromActivity(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setActivityDate(activity.getActivityDate());
        response.setStepsCount(activity.getStepsCount());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setWorkoutType(activity.getWorkoutType());
        response.setWorkoutDurationMinutes(activity.getWorkoutDurationMinutes());
        response.setDistanceKm(activity.getDistanceKm());
        response.setNotes(activity.getNotes());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}