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
import com.healthconnect.service.UserService;
import com.healthconnect.transfer.response.MessageResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
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
			user.setPassword(null); // not returning the password in response
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateUserProfile(@RequestBody User userUpdates) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		try {
			User updateUser = userService.updateUser(userDetails.getId(), userUpdates);

			updateUser.setPassword(null);
			return ResponseEntity.ok(updateUser);
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
