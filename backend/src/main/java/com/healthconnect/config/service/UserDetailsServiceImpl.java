package com.healthconnect.config.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthconnect.entity.User;
import com.healthconnect.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> userByUsername = userRepository.findByUsername(usernameOrEmail);

		Optional<User> user = userByUsername.isPresent() ? 
				userByUsername : 
				userRepository.findByEmail(usernameOrEmail);

		if(!user.isPresent()) {
			throw new UsernameNotFoundException("User not found with either username or email: "+ usernameOrEmail);
		}
		return UserDetailsImpl.build(user.get());

	}

}
