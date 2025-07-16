package com.healthconnect.transfer.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.healthconnect.entity.Mood;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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