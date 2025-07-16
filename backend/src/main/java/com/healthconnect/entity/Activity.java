package com.healthconnect.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "activities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotNull
    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;
    
    @Column(name = "steps_count")
    @Min(0)
    private Integer stepsCount;
    
    @Column(name = "calories_burned")
    @Min(0)
    private Integer caloriesBurned;
    
    @Column(name = "workout_type", length = 50)
    private String workoutType;
    
    @Column(name = "workout_duration_minutes")
    @Min(0)
    private Integer workoutDurationMinutes;
    
    @Column(name = "distance_km")
    @Min(0)
    private Double distanceKm;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}