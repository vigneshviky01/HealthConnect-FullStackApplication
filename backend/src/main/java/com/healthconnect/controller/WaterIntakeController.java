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

import com.healthconnect.service.WaterIntakeService;
import com.healthconnect.transfer.request.WaterIntakeRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.WaterIntakeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/water")
@Tag(name = "Water Intake", description = "Endpoints for managing user water intake records")
public class WaterIntakeController extends BaseController {

    @Autowired
    private WaterIntakeService waterIntakeService;

    @Operation(summary = "Create a new water intake record for the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WaterIntakeResponse> createWaterIntake(@Valid @RequestBody WaterIntakeRequest request) {
        Long userId = getCurrentUserId();
        WaterIntakeResponse waterIntake = waterIntakeService.createWaterIntake(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(waterIntake);
    }

    @Operation(summary = "Get a specific water intake record by ID for the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWaterIntake(@PathVariable("id") Long waterIntakeId) {
        Long userId = getCurrentUserId();
        return waterIntakeService.getWaterIntakeById(waterIntakeId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all water intake records for the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WaterIntakeResponse>> getAllWaterIntakes() {
        Long userId = getCurrentUserId();
        List<WaterIntakeResponse> waterIntakes = waterIntakeService.getAllWaterIntakes(userId, null);
        return ResponseEntity.ok(waterIntakes);
    }

    @Operation(summary = "Get water intake records for the authenticated user for a specific date")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/by-date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WaterIntakeResponse>> getWaterIntakesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate intakeDate) {
        Long userId = getCurrentUserId();
        List<WaterIntakeResponse> waterIntakes = waterIntakeService.getAllWaterIntakes(userId, intakeDate);
        return ResponseEntity.ok(waterIntakes);
    }

    @Operation(summary = "Update a specific water intake record by ID for the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WaterIntakeResponse> addWaterToExistingRecord(
            @PathVariable("id") Long waterIntakeId,
            @RequestBody WaterIntakeRequest request) {
        Long userId = getCurrentUserId();
        WaterIntakeResponse waterIntake = waterIntakeService.addWaterToExistingRecord(waterIntakeId, userId, request.getAmountLiters());
        return ResponseEntity.ok(waterIntake);
    }



}