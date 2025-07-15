package com.healthconnect.entity;

// Entity representing a user's activity record
import com.healthconnect.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Lombok annotations to generate getters, setters, and constructors
@Data
@Entity
@Table(name = "activities") // Maps to the 'activities' table in the database
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    // Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Many-to-one relationship with User entity, lazy-loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to users table
    private User user;

    // Date of the activity, cannot be null
    @NotNull
    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    // Number of steps, must be non-negative
    @Column(name = "steps_count")
    @Min(0)
    private Integer stepsCount;

    // Calories burned, must be non-negative
    @Column(name = "calories_burned")
    @Min(0)
    private Integer caloriesBurned;

    // Type of workout (e.g., running, cycling), max length 50
    @Column(name = "workout_type", length = 50)
    private String workoutType;

    // Duration of workout in minutes, must be non-negative
    @Column(name = "workout_duration_minutes")
    @Min(0)
    private Integer workoutDurationMinutes;

    // Distance covered in kilometers, must be non-negative
    @Column(name = "distance_km")
    @Min(0)
    private Double distanceKm;

    // Optional notes, max length 500
    @Column(name = "notes", length = 500)
    private String notes;

    // Timestamp when record is created, not updatable
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp when record is last updated
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Sets createdAt and updatedAt before persisting
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Updates updatedAt before updating
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public @NotNull LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(@NotNull LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public @Min(0) Integer getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(@Min(0) Integer stepsCount) {
        this.stepsCount = stepsCount;
    }

    public @Min(0) Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(@Min(0) Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public @Min(0) Integer getWorkoutDurationMinutes() {
        return workoutDurationMinutes;
    }

    public void setWorkoutDurationMinutes(@Min(0) Integer workoutDurationMinutes) {
        this.workoutDurationMinutes = workoutDurationMinutes;
    }

    public @Min(0) Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(@Min(0) Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
