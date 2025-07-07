package com.healthconnect.service;

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

@Service
public class WaterIntakeService {

    @Autowired
    private WaterIntakeRepository waterIntakeRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public WaterIntakeResponse createWaterIntake(Long userId, @Valid WaterIntakeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        WaterIntake waterIntake = new WaterIntake();
        waterIntake.setUser(user);
        waterIntake.setIntakeDate(request.getIntakeDate());
        waterIntake.setAmountLiters(request.getAmountLiters());
        WaterIntake saved = waterIntakeRepository.save(waterIntake);
        return WaterIntakeResponse.fromWaterIntake(saved);
    }

    public Optional<WaterIntakeResponse> getWaterIntakeById(Long waterIntakeId, Long userId) {
        Optional<WaterIntake> waterIntakeOpt = waterIntakeRepository.findById(waterIntakeId);
        if (waterIntakeOpt.isPresent() && waterIntakeOpt.get().getUser().getId().equals(userId)) {
            return Optional.of(WaterIntakeResponse.fromWaterIntake(waterIntakeOpt.get()));
        }
        return Optional.empty();
    }

    public List<WaterIntakeResponse> getAllWaterIntakes(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<WaterIntake> waterIntakes;
        if (startDate != null && endDate != null) {
            waterIntakes = waterIntakeRepository.findByUserAndIntakeDateBetween(user, startDate, endDate);
        } else {
            waterIntakes = waterIntakeRepository.findByUser(user);
        }
        return waterIntakes.stream()
                .map(WaterIntakeResponse::fromWaterIntake)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<WaterIntakeResponse> updateWaterIntake(Long waterIntakeId, Long userId, @Valid WaterIntakeRequest request) {
        Optional<WaterIntake> waterIntakeOpt = waterIntakeRepository.findById(waterIntakeId);
        if (waterIntakeOpt.isPresent() && waterIntakeOpt.get().getUser().getId().equals(userId)) {
            WaterIntake waterIntake = waterIntakeOpt.get();
            waterIntake.setIntakeDate(request.getIntakeDate());
            waterIntake.setAmountLiters(request.getAmountLiters());
            WaterIntake updated = waterIntakeRepository.save(waterIntake);
            return Optional.of(WaterIntakeResponse.fromWaterIntake(updated));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteWaterIntake(Long waterIntakeId, Long userId) {
        Optional<WaterIntake> waterIntakeOpt = waterIntakeRepository.findById(waterIntakeId);
        if (waterIntakeOpt.isPresent() && waterIntakeOpt.get().getUser().getId().equals(userId)) {
            waterIntakeRepository.delete(waterIntakeOpt.get());
            return true;
        }
        return false;
    }
}