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

import com.healthconnect.service.ActivityService;
import com.healthconnect.transfer.request.ActivityRequest;
import com.healthconnect.transfer.response.ActivityResponse;
import com.healthconnect.transfer.response.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/activities")
@Tag(name = "Activity", description = "Endpoints for managing user activity records")
public class ActivityController extends BaseController {

	@Autowired
	private ActivityService activityService;

	@Operation(summary = "Get all activity records for the authenticated user, optionally filtered by date range.")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ActivityResponse>> getAllActivities(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(defaultValue = "desc") String sort) {
		Long userId = getCurrentUserId();
		List<ActivityResponse> activities = activityService.getAllActivities(userId, startDate, endDate, sort);

		return ResponseEntity.ok(activities);
	}



	@Operation(summary = "Get a specific activity record by ID for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getActivity(@PathVariable("id") Long activityId) {
		Long userId = getCurrentUserId();
		return activityService.getActivityById(activityId, userId).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Create a new activity record for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest request) {
		Long userId = getCurrentUserId();
		ActivityResponse activity = activityService.createActivity(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(activity);
	}

	@Operation(summary = "Update a specific activity record by ID for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateActivity(@PathVariable("id") Long activityId,
			@Valid @RequestBody ActivityRequest request) {

		Long userId = getCurrentUserId();
		return activityService.updateActivity(userId, activityId, request).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Delete a specific activity record by ID for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
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

}