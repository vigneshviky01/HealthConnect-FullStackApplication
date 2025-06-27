package com.healthconnect.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
import com.healthconnect.service.SleepService;
import com.healthconnect.transfer.request.SleepRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.SleepResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sleep")
public class SleepController {

	@Autowired
	private SleepService sleepService;
	
	@GetMapping
	@PreAuthorize("isAuthenticated")
	public ResponseEntity<List<SleepResponse>> getAllSleepRecord(
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end,
			@RequestParam(defaultValue = "desc") String sort){
		Long userId = getCurrentUserId();
		List<SleepResponse> sleepRecords = sleepService.getAllSleepRecords(userId, start, end, sort);
		
		return ResponseEntity.ok(sleepRecords);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated")
	public ResponseEntity<?> getSleepRecord(@PathVariable("id") Long sleepId){
		Long userId = getCurrentUserId();
		return sleepService.getSleepById(sleepId, userId)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@PreAuthorize("isAuthenticated")
	public ResponseEntity<SleepResponse> createSleepRecord(@Valid @RequestBody SleepRequest request) {
		Long userId = getCurrentUserId();
		SleepResponse sleepRecord = sleepService.createSleepRecord(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(sleepRecord);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated")
	public ResponseEntity<?> updateSleepRecord(@PathVariable("id") Long sleepId, @Valid @RequestBody SleepRequest request){
		Long userId = getCurrentUserId();
		return sleepService.updateSleepRecord(userId, sleepId, request)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated")
	public ResponseEntity<?> deleteSleepRecord(@PathVariable("id") Long sleepId){
		Long userId = getCurrentUserId();
		boolean deleted = sleepService.deleteSleepRecord(sleepId, userId);
		if(deleted) {
			return ResponseEntity.ok(new MessageResponse("Sleep Record Deleted Successfullt !"));
		}else {
			return ResponseEntity.notFound().build();
		}
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return userDetails.getId();
	}
}
