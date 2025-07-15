package com.healthconnect.entity;

// Entity representing a user's mood record
import com.healthconnect.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
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
@Table(name = "mood") // Maps to the 'mood' table in the database
@NoArgsConstructor
@AllArgsConstructor
public class Mood {

    // Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Many-to-one relationship with User entity, lazy-loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to users table
    private User user;

    // Date of the mood record, cannot be null
    @NotNull
    @Column(name = "mood_date", nullable = false)
    private LocalDate moodDate;

    // Mood rating between 1 and 5
    @Min(1)
    @Max(5)
    @Column(name = "mood_rating", nullable = false)
    private Integer moodRating;

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

    // Getter for moodDate with validation
    public @NotNull LocalDate getMoodDate() {
        return moodDate;
    }

    // Setter for moodDate with validation
    public void setMoodDate(@NotNull LocalDate moodDate) {
        this.moodDate = moodDate;
    }

    // Getter for moodRating with validation
    public @Min(1) @Max(5) Integer getMoodRating() {
        return moodRating;
    }

    // Setter for moodRating with validation
    public void setMoodRating(@Min(1) @Max(5) Integer moodRating) {
        this.moodRating = moodRating;
    }

    // Getter for notes
    public String getNotes() {
        return notes;
    }

    // Setter for notes
    public void setNotes(String notes) {
        this.notes = notes;
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
