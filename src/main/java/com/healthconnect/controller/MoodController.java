package com.healthconnect.controller;

import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.transfer.request.MoodRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.MoodResponse;
import com.healthconnect.service.MoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mood")
@Tag(name = "Mood", description = "Endpoints for managing user mood records")
public class MoodController {

    @Autowired
    private MoodService moodService;

    @Operation(summary = "Create a new mood record for the authenticated user")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MoodResponse> createMood(@Valid @RequestBody MoodRequest request) {
        Long userId = getCurrentUserId();
        MoodResponse mood = moodService.createMood(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mood);
    }

    @Operation(summary = "Get a specific mood record by ID for the authenticated user")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMood(@PathVariable("id") Long moodId) {
        Long userId = getCurrentUserId();
        return moodService.getMoodById(moodId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all mood records for the authenticated user, optionally filtered by date range")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MoodResponse>> getAllMoods(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = getCurrentUserId();
        List<MoodResponse> moods = moodService.getAllMoods(userId, startDate, endDate);
        return ResponseEntity.ok(moods);
    }

    @Operation(summary = "Update a specific mood record by ID for the authenticated user")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMood(
            @PathVariable("id") Long moodId,
            @Valid @RequestBody MoodRequest request) {
        Long userId = getCurrentUserId();
        return moodService.updateMood(moodId, userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a specific mood record by ID for the authenticated user")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteMood(@PathVariable("id") Long moodId) {
        Long userId = getCurrentUserId();
        boolean deleted = moodService.deleteMood(moodId, userId);
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Mood record deleted successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get mood records for the authenticated user for a specific date")
    @GetMapping("/by-date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MoodResponse>> getMoodsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate moodDate) {
        Long userId = getCurrentUserId();
        List<MoodResponse> moods = moodService.getMoodsByDate(userId, moodDate);
        return ResponseEntity.ok(moods);
    }

    @Operation(summary = "Get mood records for the authenticated user with rating greater than specified value")
    @GetMapping("/by-rating")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MoodResponse>> getMoodsByMinRating(
            @RequestParam Integer minMoodRating) {
        Long userId = getCurrentUserId();
        List<MoodResponse> moods = moodService.getMoodsByMinRating(userId, minMoodRating);
        return ResponseEntity.ok(moods);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}