package com.healthconnect.transfer.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterIntakeRequest {

    @NotNull
    private LocalDate intakeDate;

    @Min(value = 0, message = "Amount must be non-negative")
    private Double amountLiters;

    public @NotNull LocalDate getIntakeDate() {
        return intakeDate;
    }

    public void setIntakeDate(@NotNull LocalDate intakeDate) {
        this.intakeDate = intakeDate;
    }

    public @Min(value = 0, message = "Amount must be non-negative") Double getAmountLiters() {
        return amountLiters;
    }

    public void setAmountLiters(@Min(value = 0, message = "Amount must be non-negative") Double amountLiters) {
        this.amountLiters = amountLiters;
    }
}