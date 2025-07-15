package com.healthconnect.service;

// Service layer for handling mood-related business logic
import com.healthconnect.entity.User;
import com.healthconnect.entity.Mood;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.repository.MoodRepository;
import com.healthconnect.transfer.request.MoodRequest;
import com.healthconnect.transfer.response.MoodResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Marks this as a Spring service
@Service
public class MoodService {

    // Injects MoodRepository for database operations
    @Autowired
    private MoodRepository moodRepository;

    // Injects UserRepository to fetch user details
    @Autowired
    private UserRepository userRepository;

    // Creates a new mood record
    @Transactional // Ensures database operations are atomic
    public MoodResponse createMood(@Valid MoodRequest request, Long userId) {
        // Validate and fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and populate new mood
        Mood mood = new Mood();
        mood.setUser(user);
        mood.setMoodDate(request.getMoodDate());
        mood.setMoodRating(request.getMoodRating());
        mood.setNotes(request.getNotes());

        // Save to database
        Mood saved = moodRepository.save(mood);
        return MoodResponse.fromMood(saved);
    }

    // Retrieves a specific mood record by ID
    public Optional<MoodResponse> getMoodById(Long moodId, Long userId) {
        Optional<Mood> moodOpt = moodRepository.findById(moodId);
        if (moodOpt.isPresent() && moodOpt.get().getUser().getId().equals(userId)) {
            // Verify mood ownership
            return Optional.of(MoodResponse.fromMood(moodOpt.get()));
        }
        return Optional.empty();
    }

    // Retrieves all mood records with optional date range filter
    public List<MoodResponse> getAllMoods(Long userId, LocalDate startDate, LocalDate endDate) {
        // Validate and fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Mood> moods;
        // Apply date range filter if provided
        if (startDate != null && endDate != null) {
            moods = moodRepository.findByUserAndMoodDateBetween(user, startDate, endDate);
        } else {
            moods = moodRepository.findByUser(user);
        }

        // Convert to response objects
        return moods.stream()
                .map(MoodResponse::fromMood)
                .collect(Collectors.toList());
    }

    // Updates an existing mood record
    @Transactional
    public Optional<MoodResponse> updateMood(Long moodId, Long userId, @Valid MoodRequest request) {
        Optional<Mood> moodOpt = moodRepository.findById(moodId);
        if (moodOpt.isPresent() && moodOpt.get().getUser().getId().equals(userId)) {
            // Verify mood ownership
            Mood mood = moodOpt.get();

            // Update mood fields
            mood.setMoodDate(request.getMoodDate());
            mood.setMoodRating(request.getMoodRating());
            mood.setNotes(request.getNotes());

            // Save to database
            Mood updated = moodRepository.save(mood);
            return Optional.of(MoodResponse.fromMood(updated));
        }
        return Optional.empty();
    }

    // Deletes a mood record
    @Transactional
    public boolean deleteMood(Long moodId, Long userId) {
        Optional<Mood> moodOpt = moodRepository.findById(moodId);
        if (moodOpt.isPresent() && moodOpt.get().getUser().getId().equals(userId)) {
            // Verify mood ownership
            moodRepository.delete(moodOpt.get());
            return true;
        }
        return false;
    }
}
