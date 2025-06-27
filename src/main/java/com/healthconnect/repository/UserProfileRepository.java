package com.healthconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthconnect.entity.User;
import com.healthconnect.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

	Optional<UserProfile> findByUser(User user);

	Optional<UserProfile> findByUserId(Long id);
}
