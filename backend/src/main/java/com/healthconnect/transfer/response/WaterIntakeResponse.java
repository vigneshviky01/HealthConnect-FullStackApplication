package com.healthconnect.transfer.response;

import com.healthconnect.entity.WaterIntake;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterIntakeResponse {

    private Long id;
    private LocalDate intakeDate;
    private Double amountLiters;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static WaterIntakeResponse fromWaterIntake(WaterIntake waterIntake) {
        return new WaterIntakeResponse(
                waterIntake.getId(),
                waterIntake.getIntakeDate(),
                waterIntake.getAmountLiters(),
                waterIntake.getCreatedAt(),
                waterIntake.getUpdatedAt()
        );
    }
}