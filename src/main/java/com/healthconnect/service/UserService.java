package com.healthconnect.service;

//import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthconnect.entity.User;
import com.healthconnect.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
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
	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Transactional
	public User updateUser(Long id, User user) {
		User newUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + id));

		if (user.getName() != null && !user.getName().isEmpty()) {
			newUser.setName(user.getName());
		}
		if (user.getGender() != null) {
			newUser.setGender(user.getGender());
		}
		if (user.getAge() != null) {
			newUser.setAge(user.getAge());
		}
		if (user.getWeight() != null) {
			newUser.setWeight(user.getWeight());
		}
		if (user.getHeight() != null) {
			newUser.setHeight(user.getHeight());
		}

		return userRepository.save(newUser);
	}

	@Transactional
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

}
