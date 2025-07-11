package com.healthconnect.controller;

import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.transfer.request.WaterIntakeRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.WaterIntakeResponse;
import com.healthconnect.service.WaterIntakeService;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/water")
@Tag(name = "Water Intake", description = "Endpoints for managing user water intake records")
public class WaterIntakeController {

    @Autowired
    private WaterIntakeService waterIntakeService;

    @Operation(summary = "Create a new water intake record for the authenticated user")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WaterIntakeResponse> createWaterIntake(@Valid @RequestBody WaterIntakeRequest request) {
        Long userId = getCurrentUserId();
        WaterIntakeResponse waterIntake = waterIntakeService.createWaterIntake(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(waterIntake);
    }

    @Operation(summary = "Get a specific water intake record by ID for the authenticated user")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWaterIntake(@PathVariable("id") Long waterIntakeId) {
        Long userId = getCurrentUserId();
        return waterIntakeService.getWaterIntakeById(waterIntakeId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all water intake records for the authenticated user")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WaterIntakeResponse>> getAllWaterIntakes() {
        Long userId = getCurrentUserId();
        List<WaterIntakeResponse> waterIntakes = waterIntakeService.getAllWaterIntakes(userId, null);
        return ResponseEntity.ok(waterIntakes);
    }

    @Operation(summary = "Get water intake records for the authenticated user for a specific date")
    @GetMapping("/by-date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WaterIntakeResponse>> getWaterIntakesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate intakeDate) {
        Long userId = getCurrentUserId();
        List<WaterIntakeResponse> waterIntakes = waterIntakeService.getAllWaterIntakes(userId, intakeDate);
        return ResponseEntity.ok(waterIntakes);
    }

    @Operation(summary = "Update a specific water intake record by ID for the authenticated user")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateWaterIntake(
            @PathVariable("id") Long waterIntakeId,
            @Valid @RequestBody WaterIntakeRequest request) {
        Long userId = getCurrentUserId();
        return waterIntakeService.updateWaterIntake(waterIntakeId, userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a specific water intake record by ID for the authenticated user")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteWaterIntake(@PathVariable("id") Long waterIntakeId) {
        Long userId = getCurrentUserId();
        boolean deleted = waterIntakeService.deleteWaterIntake(waterIntakeId, userId);
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Water intake record deleted successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add amount of water to an existing water intake record by ID")
    @PostMapping("/{id}/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WaterIntakeResponse> addWaterToExistingRecord(
            @PathVariable("id") Long waterIntakeId,
            @RequestBody WaterIntakeRequest request) {
        Long userId = getCurrentUserId();
        WaterIntakeResponse waterIntake = waterIntakeService.addWaterToExistingRecord(waterIntakeId, userId, request.getAmountLiters());
        return ResponseEntity.ok(waterIntake);
    }

    @Operation(summary = "Get water intake redcords for the authenticate user where amount is greater than specified value")
    @GetMapping("/by-amount")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WaterIntakeResponse>> getWaterIntakesByMinAmount(
            @RequestParam Double intakeAmount) {
        Long userId = getCurrentUserId();
        List<WaterIntakeResponse> waterIntakes = waterIntakeService.getWaterIntakesByMinAmount(userId, intakeAmount);
        return ResponseEntity.ok(waterIntakes);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}