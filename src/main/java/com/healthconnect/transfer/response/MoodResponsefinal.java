package com.healthconnect.transfer.response;

// DTO for mood response
import com.healthconnect.entity.Mood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Lombok annotations to generate getters, setters, and constructors
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodResponse {

    private Long id;
    private LocalDate moodDate;
    private Integer moodRating;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Parameterized Constructor
    public MoodResponse(Long id, LocalDate moodDate, Integer moodRating, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.moodDate = moodDate;
        this.moodRating = moodRating;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Converts Mood entity to MoodResponse DTO
    public static MoodResponse fromMood(Mood mood) {
        return new MoodResponse(
                mood.getId(),
                mood.getMoodDate(),
                mood.getMoodRating(),
                mood.getNotes(),
                mood.getCreatedAt(),
                mood.getUpdatedAt()
        );
    }
}
