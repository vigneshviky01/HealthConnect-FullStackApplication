package com.healthconnect.controller;


import com.healthconnect.config.security.JwtUtils;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.User;
import com.healthconnect.service.UserService;
import com.healthconnect.transfer.request.LoginRequest;
import com.healthconnect.transfer.request.SignupRequest;
import com.healthconnect.transfer.response.JwtResponse;
import com.healthconnect.transfer.response.MessageResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return ResponseEntity
				.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		// Check if username is already taken
		if (userService.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		// Check if email is already in use
		if (userService.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(signupRequest.getPassword()); // Will be encoded in service
		user.setName(signupRequest.getName());
		user.setGender(signupRequest.getGender());
		user.setAge(signupRequest.getAge());
		user.setWeight(signupRequest.getWeight());
		user.setHeight(signupRequest.getHeight());

		userService.registerUser(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
