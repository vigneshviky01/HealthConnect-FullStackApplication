package com.healthconnect.transfer.response;

import com.healthconnect.entity.UserProfile;
import com.healthconnect.entity.UserProfile.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
	private Long id;
	private String username;
	private String email;
	private String name;
	private Gender gender;
	private Integer age;
	private Double weight;
	private Double height;

	public static UserProfileResponse fromUser_Profile(Long id, String email, String username, UserProfile profile) {
		UserProfileResponse response = new UserProfileResponse();
		response.setId(id);
		response.setEmail(email);
		response.setUsername(username);

		if (profile != null) {
			response.setName(profile.getName());
			response.setGender(profile.getGender());
			response.setAge(profile.getAge());
			response.setWeight(profile.getWeight());
			response.setHeight(profile.getHeight());
		}

		return response;
	}
}
