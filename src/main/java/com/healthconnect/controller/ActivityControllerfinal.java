package com.healthconnect.controller;

// REST controller for managing user activity records
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.service.ActivityService;
import com.healthconnect.transfer.request.ActivityRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.ActivityResponse;
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

// Marks this class as a REST controller with base URL /api/activities
@RestController
@RequestMapping("/api/activities")
// Swagger tag for API documentation
@Tag(name = "Activity Controller", description = "Endpoints for managing user activity records")
public class ActivityController {

	// Injects ActivityService dependency
	@Autowired
	private ActivityService activityService;

	// Retrieves all activities with optional filters for date range, workout type, and sort order
	@Operation(summary = "Get all activity records for the authenticated user, optionally filtered by date range and workout type")
	@GetMapping
	@PreAuthorize("isAuthenticated()") // Restricts access to authenticated users
	public ResponseEntity<List<ActivityResponse>> getAllActivities(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, // Optional start date filter
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, // Optional end date filter
			@RequestParam(required = false) String workoutType, // Optional workout type filter
			@RequestParam(defaultValue = "desc") String sort) { // Sort order, defaults to descending
		Long userId = getCurrentUserId(); // Gets authenticated user's ID
		List<ActivityResponse> activities = activityService.getAllActivities(userId, startDate, endDate, workoutType, sort);
		return ResponseEntity.ok(activities); // Returns 200 OK with list of activities
	}

	// Retrieves activities for a specific date, defaulting to today if no date is provided
	@Operation(summary = "Get activity records for a specific date for the authenticated user")
	@GetMapping("/by-date")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByDate(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, // Date to query, optional
			@RequestParam(defaultValue = "desc") String sort) { // Sort order, defaults to descending
		LocalDate queryDate = (date != null) ? date : LocalDate.now(); // Uses current date if none provided
		Long userId = getCurrentUserId();
		List<ActivityResponse> activities = activityService.getActivitiesByDate(userId, queryDate, sort);
		return ResponseEntity.ok(activities);
	}

	// Retrieves a specific activity by ID
	@Operation(summary = "Get a specific activity record by ID for the authenticated user")
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getActivity(@PathVariable("id") Long activityId) { // Activity ID from URL path
		Long userId = getCurrentUserId();
		return activityService.getActivityById(activityId, userId)
				.map(ResponseEntity::ok) // Returns 200 OK with activity if found
				.orElse(ResponseEntity.notFound().build()); // Returns 404 if not found or not owned by user
	}

	// Creates a new activity record
	@Operation(summary = "Create a new activity record for the authenticated user")
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest request) { // Validated request body
		Long userId = getCurrentUserId();
		ActivityResponse activity = activityService.createActivity(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(activity); // Returns 201 Created with new activity
	}

	// Updates an existing activity record
	@Operation(summary = "Update a specific activity record by ID for the authenticated user")
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateActivity(@PathVariable("id") Long activityId, @Valid @RequestBody ActivityRequest request) {
		Long userId = getCurrentUserId();
		return activityService.updateActivity(userId, activityId, request)
				.map(ResponseEntity::ok) // Returns 200 OK with updated activity
				.orElse(ResponseEntity.notFound().build()); // Returns 404 if not found or not owned
	}

	// Deletes an activity record
	@Operation(summary = "Delete a specific activity record by ID for the authenticated user")
	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteActivity(@PathVariable("id") Long activityId) {
		Long userId = getCurrentUserId();
		boolean deleted = activityService.deleteActivity(activityId, userId);
		if (deleted) {
			return ResponseEntity.ok(new MessageResponse("Activity record deleted successfully")); // Returns 200 OK with success message
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
