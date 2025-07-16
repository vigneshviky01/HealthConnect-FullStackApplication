package com.healthconnect.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthconnect.service.MoodService;
import com.healthconnect.transfer.request.MoodRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.MoodResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mood")
@Tag(name = "Mood", description = "Endpoints for managing user mood records")
public class MoodController extends BaseController {

    @Autowired
    private MoodService moodService;

    @Operation(summary = "Create a new mood record for the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MoodResponse> createMood(@Valid @RequestBody MoodRequest request) {
        Long userId = getCurrentUserId();
        MoodResponse mood = moodService.createMood(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mood);
    }

    @Operation(summary = "Get a specific mood record by ID for the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMood(@PathVariable("id") Long moodId) {
        Long userId = getCurrentUserId();
        return moodService.getMoodById(moodId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all mood records for the authenticated user, optionally filtered by date range")
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/by-date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MoodResponse>> getMoodsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate moodDate) {
        Long userId = getCurrentUserId();
        List<MoodResponse> moods = moodService.getMoodsByDate(userId, moodDate);
        return ResponseEntity.ok(moods);
    }



}