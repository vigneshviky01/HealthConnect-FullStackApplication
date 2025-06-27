package com.healthconnect.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.User;
import com.healthconnect.entity.UserProfile;
import com.healthconnect.service.UserService;
import com.healthconnect.transfer.request.ProfileUpdateRequest;
import com.healthconnect.transfer.response.MessageResponse;
import com.healthconnect.transfer.response.UserProfileResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getUserProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		Optional<User> userOptional = userService.getUserById(userDetails.getId());

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

	@PutMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateUserProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		try {
			UserProfile updateProfile = new UserProfile();
			updateProfile.setName(profileUpdateRequest.getFullName());
			updateProfile.setGender(profileUpdateRequest.getGender());
			updateProfile.setAge(profileUpdateRequest.getAge());
			updateProfile.setWeight(profileUpdateRequest.getWeight());
			updateProfile.setHeight(profileUpdateRequest.getHeight());
			
			UserProfile updatedProfile = userService.updateUserProfile(userDetails.getId(), updateProfile);
			User user = userService.getUserById(userDetails.getId()).orElseThrow();
			
			UserProfileResponse response = UserProfileResponse.fromUser_Profile(user.getId(), user.getEmail(), user.getUsername(), updatedProfile);

			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
		}
	}

	@DeleteMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteUserAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		userService.deleteUser(userDetails.getId());
		return ResponseEntity.ok(new MessageResponse("Your Account Deleted Successfully."));
	}

}
