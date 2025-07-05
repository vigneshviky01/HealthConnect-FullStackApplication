package com.healthconnect.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.service.ActivityService;
import com.healthconnect.transfer.request.ActivityRequest;
import com.healthconnect.transfer.response.ActivityResponse;
import com.healthconnect.transfer.response.MessageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	// Get all activities with optional filtering
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ActivityResponse>> getAllActivities(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) String workoutType, @RequestParam(defaultValue = "desc") String sort) {
		Long userId = getCurrentUserId();
		List<ActivityResponse> activities = activityService.getAllActivities(userId, startDate, endDate, workoutType,
				sort);

		return ResponseEntity.ok(activities);
	}

	// Get activities for a specific date
	@GetMapping("/by-date")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByDate(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(defaultValue = "desc") String sort) {

		LocalDate queryDate = (date != null) ? date : LocalDate.now();
		Long userId = getCurrentUserId();
		List<ActivityResponse> activities = activityService.getActivitiesByDate(userId, queryDate, sort);

		return ResponseEntity.ok(activities);
	}

	// Get a specific activity by ID
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getActivity(@PathVariable("id") Long activityId) {
		Long userId = getCurrentUserId();
		return activityService.getActivityById(activityId, userId).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Create a new activity record
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest request) {
		Long userId = getCurrentUserId();
		ActivityResponse activity = activityService.createActivity(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(activity);
	}

	// Update an existing activity record
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateActivity(@PathVariable("id") Long activityId,
			@Valid @RequestBody ActivityRequest request) {

		Long userId = getCurrentUserId();
		return activityService.updateActivity(userId, activityId, request).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Delete an activity record
	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteActivity(@PathVariable("id") Long activityId) {
		Long userId = getCurrentUserId();
		boolean deleted = activityService.deleteActivity(activityId, userId);

		if (deleted) {
			return ResponseEntity.ok(new MessageResponse("Activity record deleted successfully"));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Helper method to get the current user ID from the security context
	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return userDetails.getId();
	}
}