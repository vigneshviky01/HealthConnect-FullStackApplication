package com.healthconnect.transfer.request;

// DTO for activity creation and update requests
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Lombok annotations to generate getters, setters, and constructors
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {

    // Date of the activity, required
    @NotNull
    private LocalDate activityDate;

    // Number of steps, must be non-negative
    @Min(value = 0)
    private Integer stepsCount;

    // Calories burned, must be non-negative
    @Min(value = 0)
    private Integer caloriesBurned;

    // Type of workout (e.g., running, cycling)
    private String workoutType;

    // Duration of workout in minutes, must be non-negative
    @Min(value = 0)
    private Integer workoutDurationMinutes;

    // Distance covered in kilometers, must be non-negative
    @Min(value = 0)
    private Double distanceKm;

    // Optional notes
    private String notes;

    public @NotNull LocalDate getActivityDate() {
        return activityDate;
    }

    public @Min(value = 0) Integer getStepsCount() {
        return stepsCount;
    }

    public @Min(value = 0) Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public @Min(value = 0) Integer getWorkoutDurationMinutes() {
        return workoutDurationMinutes;
    }

    public @Min(value = 0) Double getDistanceKm() {
        return distanceKm;
    }

    public String getNotes() {
        return notes;
    }
}
