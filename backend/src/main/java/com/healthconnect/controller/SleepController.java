package com.healthconnect.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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

import com.healthconnect.service.SleepService;
import com.healthconnect.transfer.request.SleepRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.SleepResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sleep")
@Tag(name = "Sleep", description = "Endpoints for managing user sleep records")
public class SleepController extends BaseController {

	@Autowired
	private SleepService sleepService;
	
	@Operation(summary = "Get all sleep records for the authenticated user, optionally filtered by date/time and quality")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<SleepResponse>> getAllSleepRecord(
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end,
			@RequestParam(defaultValue = "desc") String sort){
		Long userId = getCurrentUserId();
		List<SleepResponse> sleepRecords = sleepService.getAllSleepRecords(userId, start, end, sort);
		
		return ResponseEntity.ok(sleepRecords);
	}

	// Get sleep records for a specific date
	@Operation(summary = "Get sleep records for a specific date for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/by-date")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<SleepResponse>> getSleepRecordsByDate(
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate date,
			@RequestParam(defaultValue = "desc") String sort) {
		LocalDate queryDate = (date != null) ? date : LocalDate.now();
		Long userId = getCurrentUserId();
		List<SleepResponse> sleepRecords = sleepService.getSleepRecordsByDate(userId, queryDate, sort);
		return ResponseEntity.ok(sleepRecords);
	}
	
	@Operation(summary = "Get a specific sleep record by ID for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getSleepRecord(@PathVariable("id") Long sleepId){
		Long userId = getCurrentUserId();
		return sleepService.getSleepById(sleepId, userId)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Create a new sleep record for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<SleepResponse> createSleepRecord(@Valid @RequestBody SleepRequest request) {
		Long userId = getCurrentUserId();
		SleepResponse sleepRecord = sleepService.createSleepRecord(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(sleepRecord);
	}
	
	@Operation(summary = "Update a specific sleep record by ID for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateSleepRecord(@PathVariable("id") Long sleepId, @Valid @RequestBody SleepRequest request){
		Long userId = getCurrentUserId();
		return sleepService.updateSleepRecord(userId, sleepId, request)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@Operation(summary = "Delete a specific sleep record by ID for the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteSleepRecord(@PathVariable("id") Long sleepId){
		Long userId = getCurrentUserId();
		boolean deleted = sleepService.deleteSleepRecord(sleepId, userId);
		if(deleted) {
			return ResponseEntity.ok(new MessageResponse("Sleep Record Deleted Successfullt !"));
		}else {
			return ResponseEntity.notFound().build();
		}
	}

}
