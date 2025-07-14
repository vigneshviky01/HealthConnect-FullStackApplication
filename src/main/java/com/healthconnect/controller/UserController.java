package com.healthconnect.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthconnect.entity.User;
import com.healthconnect.entity.UserProfile;
import com.healthconnect.service.UserService;
import com.healthconnect.transfer.request.ProfileUpdateRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.UserProfileResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints for user profile management")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Get the profile of the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getUserProfile() {
		Long userId = getCurrentUserId();
		Optional<User> userOptional = userService.getUserById(userId);

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			Optional<UserProfile> profileOptional = userService.getUserProfile(user);
			UserProfile profile = profileOptional.orElse(null);
			UserProfileResponse response = UserProfileResponse.fromUser_Profile(user.getId(), user.getEmail(),
					user.getUsername(), profile);

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Update the profile of the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateUserProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
		Long userId = getCurrentUserId();
		
		try {
			UserProfile updateProfile = new UserProfile();
			updateProfile.setName(profileUpdateRequest.getFullName());
			updateProfile.setGender(profileUpdateRequest.getGender());
			updateProfile.setAge(profileUpdateRequest.getAge());
			updateProfile.setWeight(profileUpdateRequest.getWeight());
			updateProfile.setHeight(profileUpdateRequest.getHeight());
			
			UserProfile updatedProfile = userService.updateUserProfile(userId, updateProfile);
			User user = userService.getUserById(userId).orElseThrow();
			
			UserProfileResponse response = UserProfileResponse.fromUser_Profile(user.getId(), user.getEmail(), user.getUsername(), updatedProfile);

			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
		}
	}

	@Operation(summary = "Delete the account of the authenticated user")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteUserAccount() {
		Long userId = getCurrentUserId();
		userService.deleteUser(userId);
		return ResponseEntity.ok(new MessageResponse("Your Account Deleted Successfully."));
	}

}
