package com.healthconnect.transfer.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodRequest {

    @NotNull
    private LocalDate moodDate;

    @Min(value = 1, message = "Mood rating must be at least 1")
    @Max(value = 5, message = "Mood rating must be at most 5")
    private Integer moodRating;

    private String notes;

    public @NotNull LocalDate getMoodDate() {
        return moodDate;
    }

    public void setMoodDate(@NotNull LocalDate moodDate) {
        this.moodDate = moodDate;
    }

    public @Min(value = 1, message = "Mood rating must be at least 1") @Max(value = 5, message = "Mood rating must be at most 5") Integer getMoodRating() {
        return moodRating;
    }

    public void setMoodRating(@Min(value = 1, message = "Mood rating must be at least 1") @Max(value = 5, message = "Mood rating must be at most 5") Integer moodRating) {
        this.moodRating = moodRating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}