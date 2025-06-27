package com.healthconnect.transfer.response;

import java.time.LocalDateTime;

import com.healthconnect.entity.Sleep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SleepResponse {
	private Long id;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Integer qualityRating;
	private String notes;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public static SleepResponse sleepResponse(Sleep sleep) {
		SleepResponse response = new SleepResponse();
		response.setId(sleep.getId());
		response.setStartTime(sleep.getSleepStartTime());
		response.setEndTime(sleep.getSleepEndTime());
        response.setQualityRating(sleep.getQualityRating());
        response.setNotes(sleep.getNotes());
        response.setCreatedAt(sleep.getCreatedAt());
        response.setUpdatedAt(sleep.getUpdatedAt());
        return response;
	}
}
