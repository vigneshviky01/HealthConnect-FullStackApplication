package com.healthconnect.service;

// Service layer for handling water intake-related business logic
import com.healthconnect.entity.User;
import com.healthconnect.entity.WaterIntake;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.repository.WaterIntakeRepository;
import com.healthconnect.transfer.request.WaterIntakeRequest;
import com.healthconnect.transfer.response.WaterIntakeResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Marks this as a Spring service
@Service
public class WaterIntakeService {

    // Injects WaterIntakeRepository for database operations
    @Autowired
    private WaterIntakeRepository waterIntakeRepository;

    // Injects UserRepository to fetch user details
    @Autowired
    private UserRepository userRepository;

    // Creates or updates a water intake record for the authenticated user
    @Transactional // Ensures database operations are atomic
    public WaterIntakeResponse createWaterIntake(Long userId, @Valid WaterIntakeRequest request) {
        // Validate and fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check for existing record for the same user and date
        Optional<WaterIntake> existingRecord = waterIntakeRepository.findByUserAndIntakeDate(user, request.getIntakeDate());

        WaterIntake waterIntake;
        if (existingRecord.isPresent()) {
            // Update existing record by adding new amount to current amount
            waterIntake = existingRecord.get();
            waterIntake.setAmountLiters(waterIntake.getAmountLiters() + request.getAmountLiters());
        } else {
            // Create new record if none exists for the date
            waterIntake = new WaterIntake();
            waterIntake.setUser(user);
            waterIntake.setIntakeDate(request.getIntakeDate());
            waterIntake.setAmountLiters(request.getAmountLiters());
        }

        // Save to database (updates existing record or creates new)
        WaterIntake saved = waterIntakeRepository.save(waterIntake);
        return WaterIntakeResponse.fromWaterIntake(saved);
    }

    // Retrieves a specific water intake record by ID
    public Optional<WaterIntakeResponse> getWaterIntakeById(Long waterIntakeId, Long userId) {
        Optional<WaterIntake> waterIntakeOpt = waterIntakeRepository.findById(waterIntakeId);
        if (waterIntakeOpt.isPresent() && waterIntakeOpt.get().getUser().getId().equals(userId)) {
            // Verify user ownership
            return Optional.of(WaterIntakeResponse.fromWaterIntake(waterIntakeOpt.get()));
        }
        return Optional.empty();
    }

    // Retrieves all water intake records with optional date range filter
    public List<WaterIntakeResponse> getAllWaterIntakes(Long userId, LocalDate startDate, LocalDate endDate) {
        // Validate and fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<WaterIntake> waterIntakes;
        // Apply date range if provided
        if (startDate != null && endDate != null) {
            waterIntakes = waterIntakeRepository.findByUserAndIntakeDateBetween(user, startDate, endDate);
        } else {
            waterIntakes = waterIntakeRepository.findByUser(user);
        }

        // Convert to response objects
        return waterIntakes.stream()
                .map(WaterIntakeResponse::fromWaterIntake)
                .collect(Collectors.toList());
    }

    // Updates an existing water intake record
    @Transactional
    public Optional<WaterIntakeResponse> updateWaterIntake(Long waterIntakeId, Long userId, @Valid WaterIntakeRequest request) {
        Optional<WaterIntake> waterIntakeOpt = waterIntakeRepository.findById(waterIntakeId);
        if (waterIntakeOpt.isPresent() && waterIntakeOpt.get().getUser().getId().equals(userId)) {
            // Verify user ownership
            WaterIntake waterIntake = waterIntakeOpt.get();

            // Check if the date is changing and if another record exists for the new date
            if (!waterIntake.getIntakeDate().equals(request.getIntakeDate())) {
                Optional<WaterIntake> existingRecord = waterIntakeRepository.findByUserAndIntakeDate(
                        waterIntake.getUser(), request.getIntakeDate());
                if (existingRecord.isPresent()) {
                    // Merge with existing record for the new date
                    WaterIntake existing = existingRecord.get();
                    existing.setAmountLiters(existing.getAmountLiters() + request.getAmountLiters());
                    waterIntakeRepository.delete(waterIntake); // Delete old record
                    waterIntake = existing;
                } else {
                    // Update date if no conflict
                    waterIntake.setIntakeDate(request.getIntakeDate());
                }
            }
            // Update amount by adding to existing (or setting if no merge)
            waterIntake.setAmountLiters(request.getAmountLiters());

            // Save to database
            WaterIntake updated = waterIntakeRepository.save(waterIntake);
            return Optional.of(WaterIntakeResponse.fromWaterIntake(updated));
        }
        return Optional.empty();
    }

    // Deletes a water intake record
    @Transactional
    public boolean deleteWaterIntake(Long waterIntakeId, Long userId) {
        Optional<WaterIntake> waterIntakeOpt = waterIntakeRepository.findById(waterIntakeId);
        if (waterIntakeOpt.isPresent() && waterIntakeOpt.get().getUser().getId().equals(userId)) {
            // Verify user ownership
            waterIntakeRepository.delete(waterIntakeOpt.get());
            return true;
        }
        return false;
    }
}
