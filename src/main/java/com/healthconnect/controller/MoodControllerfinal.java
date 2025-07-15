package com.healthconnect.controller;

// REST controller for managing user mood records
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.service.MoodService;
import com.healthconnect.transfer.request.MoodRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.MoodResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

// Marks this class as a REST controller with base URL /api/mood
@RestController
@RequestMapping("/api/mood")
// Swagger tag for API documentation
@Tag(name = "Mood Controller", description = "Endpoints for managing user mood records")
public class MoodController {

    // Injects MoodService dependency
    @Autowired
    private MoodService moodService;

    // Creates a new mood record
    @Operation(summary = "Create a new mood record for the authenticated user")
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Restricts access to authenticated users
    public ResponseEntity<MoodResponse> createMood(@Valid @RequestBody MoodRequest request) { // Validated request body
        Long userId = getCurrentUserId(); // Gets authenticated user's ID
        MoodResponse mood = moodService.createMood(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mood); // Returns 201 Created with new mood record
    }

    // Retrieves a specific mood record by ID
    @Operation(summary = "Get a specific mood record by ID for the authenticated user")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMood(@PathVariable("id") Long moodId) { // Mood ID from URL path
        Long userId = getCurrentUserId();
        return moodService.getMoodById(moodId, userId)
                .map(ResponseEntity::ok) // Returns 200 OK with mood if found
                .orElse(ResponseEntity.notFound().build()); // Returns 404 if not found or not owned
    }

    // Retrieves all mood records with optional date range filter
    @Operation(summary = "Get all mood records for the authenticated user, optionally filtered by date range")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MoodResponse>> getAllMoods(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, // Optional start date filter
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { // Optional end date filter
        Long userId = getCurrentUserId();
        List<MoodResponse> moods = moodService.getAllMoods(userId, startDate, endDate);
        return ResponseEntity.ok(moods); // Returns 200 OK with list of moods
    }

    // Updates an existing mood record
    @Operation(summary = "Update a specific mood record by ID for the authenticated user")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMood(@PathVariable("id") Long moodId, @Valid @RequestBody MoodRequest request) {
        Long userId = getCurrentUserId();
        return moodService.updateMood(moodId, userId, request)
                .map(ResponseEntity::ok) // Returns 200 OK with updated mood
                .orElse(ResponseEntity.notFound().build()); // Returns 404 if not found or not owned
    }

    // Deletes a mood record
    @Operation(summary = "Delete a specific mood record by ID for the authenticated user")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteMood(@PathVariable("id") Long moodId) {
        Long userId = getCurrentUserId();
        boolean deleted = moodService.deleteMood(moodId, userId);
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Mood record deleted successfully")); // Returns 200 OK with success message
        } else {
            return ResponseEntity.notFound().build(); // Returns 404 if not found or not owned
        }
    }

    // Extracts the authenticated user's ID from the security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}
