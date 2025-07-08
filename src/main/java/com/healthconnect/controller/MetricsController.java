package com.healthconnect.controller;

import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.User;
import com.healthconnect.service.MetricsService;
import com.healthconnect.service.UserService;
import com.healthconnect.transfer.response.MetricsResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/metrics")
@Tag(name = "Statistics", description = "All the health metrics' statistics")
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private UserService userService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Operation(summary = "Get weekly metrics for the authenticated user")
    @GetMapping("/weekly")
    public ResponseEntity<MetricsResponse> getWeeklyMetrics() {
        User currentUser = getCurrentUser();
        MetricsResponse metrics = metricsService.getWeeklyMetrics(currentUser);
        return ResponseEntity.ok(metrics);
    }

    @Operation(summary = "Get monthly metrics for the authenticated user")
    @GetMapping("/monthly")
    public ResponseEntity<MetricsResponse> getMonthlyMetrics() {
        User currentUser = getCurrentUser();
        MetricsResponse metrics = metricsService.getMonthlyMetrics(currentUser);
        return ResponseEntity.ok(metrics);
    }

    @Operation(summary = "Get metrics for a custom date range for the authenticated user")
    @GetMapping("/custom")
    public ResponseEntity<MetricsResponse> getCustomRangeMetrics(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {
        
        User currentUser = getCurrentUser();
        LocalDate startDate = LocalDate.parse(startDateStr, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);
        
        // Validate date range
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        MetricsResponse metrics = metricsService.getMetricsForDateRange(
                currentUser, startDate, endDate, "CUSTOM");
        
        return ResponseEntity.ok(metrics);
    }

    //Helper method to get the current authenticated user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userService.getUserById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}