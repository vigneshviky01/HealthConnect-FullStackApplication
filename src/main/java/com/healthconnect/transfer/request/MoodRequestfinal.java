package com.healthconnect.transfer.request;

// DTO for mood creation and update requests
import jakarta.validation.constraints.Max;
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
public class MoodRequest {

    // Date of the mood record, required
    @NotNull
    private LocalDate moodDate;

    // Mood rating between 1 and 5, required
    @Min(value = 1, message = "Mood rating must be at least 1")
    @Max(value = 5, message = "Mood rating must be at most 5")
    private Integer moodRating;

    // Optional notes
    private String notes;

    // Getter for moodDate with validation
    public @NotNull LocalDate getMoodDate() {
        return moodDate;
    }

    // Setter for moodDate with validation
    public void setMoodDate(@NotNull LocalDate moodDate) {
        this.moodDate = moodDate;
    }

    // Getter for moodRating with validation
    public @Min(value = 1, message = "Mood rating must be at least 1") @Max(value = 5, message = "Mood rating must be at most 5") Integer getMoodRating() {
        return moodRating;
    }

    // Setter for moodRating with validation
    public void setMoodRating(@Min(value = 1, message = "Mood rating must be at least 1") @Max(value = 5, message = "Mood rating must be at most 5") Integer moodRating) {
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
}
