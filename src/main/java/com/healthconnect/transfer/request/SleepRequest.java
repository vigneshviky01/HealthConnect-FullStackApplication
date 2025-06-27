package com.healthconnect.transfer.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SleepRequest {
	
	 @NotNull
	    private LocalDateTime sleepStartTime;
	    
	    @NotNull
	    private LocalDateTime sleepEndTime;
	    
	    @Min(1)
	    @Max(5)
	    private Integer qualityRating;
	    
	    private String notes;

}
