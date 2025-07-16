package com.healthconnect.controller;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.User;
import com.healthconnect.service.MetricsService;
import com.healthconnect.service.UserService;
import com.healthconnect.transfer.response.MetricsResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/metrics")
@Tag(name = "Statistics", description = "All the health metrics' statistics")
public class MetricsController extends BaseController {

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private UserService userService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Operation(summary = "Get weekly aggregated metrics for a selected month (for graphing)")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/monthly/graph")
    public ResponseEntity<List<MetricsResponse>> getMonthlyGraphMetrics(
            @RequestParam(value = "yearMonth", required = false) String yearMonthStr,
            @RequestParam(value = "metrics", required = false) String metricsParam) {
        User currentUser = getCurrentUser();
        Set<String> metricsFilter = null;
        if (metricsParam != null && !metricsParam.isEmpty()) {
            metricsFilter = new HashSet<>(Arrays.asList(metricsParam.split(",")));
        }
        List<MetricsResponse> result;
        if (yearMonthStr != null && !yearMonthStr.isEmpty()) {
            YearMonth yearMonth = YearMonth.parse(yearMonthStr);
            result = metricsService.getFourWeekMetricsForMonth(currentUser, yearMonth, metricsFilter);
        } else {
            result = metricsService.getFourWeekMetricsFromToday(currentUser, metricsFilter);
        }
        return ResponseEntity.ok(result);
    }

    //Helper method to get the current authenticated user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userService.getUserById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}