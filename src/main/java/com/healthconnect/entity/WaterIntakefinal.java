package com.healthconnect.entity;

// Entity representing a user's water intake record
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
@Table(name = "water_intake") // Maps to the 'water_intake' table in the database
@NoArgsConstructor
@AllArgsConstructor
public class WaterIntake {

    // Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Many-to-one relationship with User entity, lazy-loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to users table
    private User user;

    // Date of water intake, cannot be null
    @NotNull
    @Column(name = "intake_date", nullable = false)
    private LocalDate intakeDate;

    // Amount of water consumed in liters, must be non-negative
    @Min(0)
    @Column(name = "amount_liters", nullable = false)
    private Double amountLiters;

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

    // Getter for ID
    public Long getId() {
        return id;
    }

    // Setter for ID
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for User
    public User getUser() {
        return user;
    }

    // Setter for User
    public void setUser(User user) {
        this.user = user;
    }

    // Getter for intakeDate with validation
    public @NotNull LocalDate getIntakeDate() {
        return intakeDate;
    }

    // Setter for intakeDate with validation
    public void setIntakeDate(@NotNull LocalDate intakeDate) {
        this.intakeDate = intakeDate;
    }

    // Getter for amountLiters with validation
    public @Min(0) Double getAmountLiters() {
        return amountLiters;
    }

    // Setter for amountLiters with validation
    public void setAmountLiters(@Min(0) Double amountLiters) {
        this.amountLiters = amountLiters;
    }

    // Getter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setter for createdAt
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getter for updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setter for updatedAt
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
