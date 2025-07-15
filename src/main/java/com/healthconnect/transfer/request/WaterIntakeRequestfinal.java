package com.healthconnect.transfer.request;

// DTO for water intake creation and update requests
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Lombok annotations to generate getters, setters, and constructors
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterIntakeRequest {

    // Date of water intake, required
    @NotNull
    private LocalDate intakeDate;

    // Amount of water in liters, must be non-negative
    @Min(value = 0, message = "Amount must be non-negative")
    private Double amountLiters;

    // Getter for intakeDate with validation
    public @NotNull LocalDate getIntakeDate() {
        return intakeDate;
    }

    // Setter for intakeDate with validation
    public void setIntakeDate(@NotNull LocalDate intakeDate) {
        this.intakeDate = intakeDate;
    }

    // Getter for amountLiters with validation
    public @Min(value = 0, message = "Amount must be non-negative") Double getAmountLiters() {
        return amountLiters;
    }

    // Setter for amountLiters with validation
    public void setAmountLiters(@Min(value = 0, message = "Amount must be non-negative") Double amountLiters) {
        this.amountLiters = amountLiters;
    }
}
