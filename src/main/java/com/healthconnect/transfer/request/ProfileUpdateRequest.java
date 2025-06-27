package com.healthconnect.transfer.request;

import com.healthconnect.entity.UserProfile.Gender;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
	private String fullName;
	private Gender gender;
	private Integer age;
	private Double weight;
	private Double height;
}
