package com.healthconnect.service;

//import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthconnect.entity.User;
import com.healthconnect.entity.UserProfile;
import com.healthconnect.repository.UserProfileRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.SignupRequest;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

//	public List<User> getAllUser(){
//		return userRepository.findAll();
//	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Transactional
	public User registerUser(SignupRequest signupRequest) {
		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		User newUser = userRepository.save(user);

		UserProfile profile = new UserProfile();
		profile.setUser(newUser);
		profile.setName(signupRequest.getName());
		profile.setGender(signupRequest.getGender());
		profile.setAge(signupRequest.getAge());
		profile.setWeight(signupRequest.getWeight());
		profile.setHeight(signupRequest.getHeight());
		userProfileRepository.save(profile);

		user.setUserProfile(profile);
		return newUser;
	}

	@Transactional
	public UserProfile updateUserProfile(Long id, UserProfile userProfile) {
		User newUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + id));

		UserProfile newProfile = userProfileRepository.findByUser(newUser)
				.orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));

		if (userProfile.getName() != null && !userProfile.getName().isEmpty())
			newProfile.setName(userProfile.getName());
		if (userProfile.getGender() != null)
			newProfile.setGender(userProfile.getGender());
		if (userProfile.getAge() != null)
			newProfile.setAge(userProfile.getAge());
		if(userProfile.getWeight() != null)
			newProfile.setWeight(userProfile.getWeight());
		if(userProfile.getHeight() != null)
			newProfile.setHeight(userProfile.getHeight());

		return userProfileRepository.save(newProfile);
	}

	// The UserProfile will be automatically deleted due to CascadeType.ALL and orphanRemoval=true
	@Transactional
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
	
	public Optional<UserProfile> getUserProfileByUserId(Long userId) {
		return userProfileRepository.findByUserId(userId);
	}
	
	public Optional<UserProfile> getUserProfile(User user) {
		return userProfileRepository.findByUser(user);
	}


}
