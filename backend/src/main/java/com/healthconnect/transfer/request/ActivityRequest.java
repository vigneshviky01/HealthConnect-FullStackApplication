package com.healthconnect.transfer.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {
    
    @NotNull
    private LocalDate activityDate;
    
    @Min(value = 0)
    private Integer stepsCount;
    
    @Min(value = 0)
    private Integer caloriesBurned;
    
    private String workoutType;
    
    @Min(value = 0)
    private Integer workoutDurationMinutes;
    
    @Min(value = 0)
    private Double distanceKm;
    
    private String notes;
}