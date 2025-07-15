package com.healthconnect.controller;

// REST controller for managing user water intake records
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.service.WaterIntakeService;
import com.healthconnect.transfer.request.WaterIntakeRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.WaterIntakeResponse;
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

// Marks this class as a REST controller with base URL /api/water
@RestController
@RequestMapping("/api/water")
// Swagger tag for API documentation
@Tag(name = "Water Intake Controller", description = "Endpoints for managing user water intake records")
public class WaterIntakeController {

    // Injects WaterIntakeService dependency
    @Autowired
    private WaterIntakeService waterIntakeService;

    // Creates or updates a water intake record, merging amounts for the same date
    @Operation(summary = "Create or update a water intake record for the authenticated user, adding amount to existing record if same date")
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Restricts access to authenticated users
    public ResponseEntity<WaterIntakeResponse> createWaterIntake(@Valid @RequestBody WaterIntakeRequest request) { // Validated request body
        Long userId = getCurrentUserId(); // Gets authenticated user's ID
        WaterIntakeResponse waterIntake = waterIntakeService.createWaterIntake(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(waterIntake); // Returns 201 Created with new or updated record
    }

    // Retrieves a specific water intake record by ID
    @Operation(summary = "Get a specific water intake record by ID for the authenticated user")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWaterIntake(@PathVariable("id") Long waterIntakeId) { // Water intake ID from URL path
        Long userId = getCurrentUserId();
        return waterIntakeService.getWaterIntakeById(waterIntakeId, userId)
                .map(ResponseEntity::ok) // Returns 200 OK with record if found
                .orElse(ResponseEntity.notFound().build()); // Returns 404 if not found or not owned
    }

    // Retrieves all water intake records with optional date range filter
    @Operation(summary = "Get all water intake records for the authenticated user, optionally filtered by date range")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WaterIntakeResponse>> getAllWaterIntakes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, // Optional start date filter
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { // Optional end date filter
        Long userId = getCurrentUserId();
        List<WaterIntakeResponse> waterIntakes = waterIntakeService.getAllWaterIntakes(userId, startDate, endDate);
        return ResponseEntity.ok(waterIntakes); // Returns 200 OK with list of records
    }

    // Updates an existing water intake record
    @Operation(summary = "Update a specific water intake record by ID for the authenticated user, merging amounts if date changes to existing record")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateWaterIntake(@PathVariable("id") Long waterIntakeId, @Valid @RequestBody WaterIntakeRequest request) {
        Long userId = getCurrentUserId();
        return waterIntakeService.updateWaterIntake(waterIntakeId, userId, request)
                .map(ResponseEntity::ok) // Returns 200 OK with updated record
                .orElse(ResponseEntity.notFound().build()); // Returns 404 if not found or not owned
    }

    // Deletes a water intake record
    @Operation(summary = "Delete a specific water intake record by ID for the authenticated user")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteWaterIntake(@PathVariable("id") Long waterIntakeId) {
        Long userId = getCurrentUserId();
        boolean deleted = waterIntakeService.deleteWaterIntake(waterIntakeId, userId);
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Water intake record deleted successfully")); // Returns 200 OK with success message
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
