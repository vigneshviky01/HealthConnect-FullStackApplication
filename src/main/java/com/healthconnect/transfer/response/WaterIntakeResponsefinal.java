package com.healthconnect.transfer.response;

// DTO for water intake response
import com.healthconnect.entity.WaterIntake;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Lombok annotations to generate getters, setters, and constructors
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterIntakeResponse {

    private Long id;
    private LocalDate intakeDate;
    private Double amountLiters;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WaterIntakeResponse(Long id, LocalDate intakeDate, Double amountLiters, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.intakeDate = intakeDate;
        this.amountLiters = amountLiters;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Converts WaterIntake entity to WaterIntakeResponse DTO
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
