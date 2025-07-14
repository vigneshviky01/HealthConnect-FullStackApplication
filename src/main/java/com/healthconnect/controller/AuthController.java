package com.healthconnect.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthconnect.config.security.JwtUtils;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.service.UserService;
import com.healthconnect.transfer.request.LoginRequest;
import com.healthconnect.transfer.request.SignupRequest;
import com.healthconnect.transfer.response.JwtResponse;
import com.healthconnect.transfer.response.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	@Operation(summary = "Authenticate user and return JWT token")
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			return ResponseEntity
					.ok(new JwtResponse(jwt, "Bearer", userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
		} catch (Exception ex) {
			return ResponseEntity.status(401).body(new MessageResponse("Invalid username/email or password."));
		}
	}

	@Operation(summary = "Register a new user account")
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

		userService.registerUser(signupRequest);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	@Operation(summary = "Logout the current user and invalidate JWT token")
	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			String jwt = headerAuth.substring(7);
			// Invalidate the token
			jwtUtils.invalidateToken(jwt);
		}
		SecurityContextHolder.clearContext();
		
		return ResponseEntity.ok(new MessageResponse("Logged out successfully!"));
	}
}
