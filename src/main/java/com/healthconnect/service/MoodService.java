package com.healthconnect.service;

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

@Service
public class MoodService {

    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public MoodResponse createMood(@Valid MoodRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Mood mood = new Mood();
        mood.setUser(user);
        mood.setMoodDate(request.getMoodDate());
        mood.setMoodRating(request.getMoodRating());
        mood.setNotes(request.getNotes());
        Mood saved = moodRepository.save(mood);
        return MoodResponse.fromMood(saved);
    }

    public Optional<MoodResponse> getMoodById(Long moodId, Long userId) {
        Optional<Mood> moodOpt = moodRepository.findById(moodId);
        if (moodOpt.isPresent() && moodOpt.get().getUser().getId().equals(userId)) {
            return Optional.of(MoodResponse.fromMood(moodOpt.get()));
        }
        return Optional.empty();
    }

    public List<MoodResponse> getAllMoods(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Mood> moods;
        if (startDate != null && endDate != null) {
            moods = moodRepository.findByUserAndMoodDateBetween(user, startDate, endDate);
        } else {
            moods = moodRepository.findByUser(user);
        }
        return moods.stream()
                .map(MoodResponse::fromMood)
                .collect(Collectors.toList());
    }

    public List<MoodResponse> getMoodsByDate(Long userId, LocalDate moodDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Mood> moods = moodRepository.findByUserAndMoodDate(user, moodDate);
        return moods.stream()
                .map(MoodResponse::fromMood)
                .collect(Collectors.toList());
    }

    public List<MoodResponse> getMoodsByMinRating(Long userId, Integer minMoodRating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Mood> moods = moodRepository.findByUserAndMoodRatingGreaterThanEqual(user, minMoodRating);
        return moods.stream()
                .map(MoodResponse::fromMood)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<MoodResponse> updateMood(Long moodId, Long userId, @Valid MoodRequest request) {
        Optional<Mood> moodOpt = moodRepository.findById(moodId);
        if (moodOpt.isPresent() && moodOpt.get().getUser().getId().equals(userId)) {
            Mood mood = moodOpt.get();
            mood.setMoodDate(request.getMoodDate());
            mood.setMoodRating(request.getMoodRating());
            mood.setNotes(request.getNotes());
            Mood updated = moodRepository.save(mood);
            return Optional.of(MoodResponse.fromMood(updated));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteMood(Long moodId, Long userId) {
        Optional<Mood> moodOpt = moodRepository.findById(moodId);
        if (moodOpt.isPresent() && moodOpt.get().getUser().getId().equals(userId)) {
            moodRepository.delete(moodOpt.get());
            return true;
        }
        return false;
    }
}