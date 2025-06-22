package com.healthconnect.transfer.request;

import com.healthconnect.entity.User.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
	@NotBlank
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String name;

	private Gender gender;

	private Integer age;

	private Double weight;

	private Double height;
}
