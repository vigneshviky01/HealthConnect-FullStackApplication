package com.healthconnect.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthconnect.entity.Sleep;
import com.healthconnect.entity.User;
import com.healthconnect.repository.SleepRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.SleepRequest;
import com.healthconnect.transfer.response.SleepResponse;

import jakarta.transaction.Transactional;

@Service
public class SleepService {

	@Autowired
	private SleepRepository sleepRepository;
	@Autowired
	private UserRepository userRepository;

	public List<SleepResponse> getAllSleepRecords(Long userId, LocalDateTime start, LocalDateTime end, String sort) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User is not found."));

		List<Sleep> sleepRecords;

		if(start != null && end != null) {
			sleepRecords = sleepRepository.findByUserAndSleepStartTimeBetween(user, start, end);
		} else {
			sleepRecords = sleepRepository.findByUser(user);
		}

		String direction = (sort == null || !sort.equalsIgnoreCase("asc")) ? "desc" : "asc";

		// Sort based on starting time of sleep...
		sleepRecords.sort((a, b) -> {
			int compare = a.getSleepStartTime().compareTo(b.getSleepStartTime());
			return direction.equals("asc") ? compare : -compare;
		});

		return sleepRecords.stream().map(SleepResponse::sleepResponse).collect(Collectors.toList());
	}
	
	public List<SleepResponse> getSleepRecordsByDate(Long userId, LocalDate date, String sort) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User is not found."));
		
		List<Sleep> sleepRecords = sleepRepository.findByUserAndDate(user, date);
		
		String direction = (sort == null || !sort.equalsIgnoreCase("asc")) ? "desc" : "asc";
		
		// Sort based on starting time of sleep
		sleepRecords.sort((a, b) -> {
			int compare = a.getSleepStartTime().compareTo(b.getSleepStartTime());
			return direction.equals("asc") ? compare : -compare;
		});
		
		return sleepRecords.stream().map(SleepResponse::sleepResponse).collect(Collectors.toList());
	}

	// Specific sleep record by id
	public Optional<SleepResponse> getSleepById(Long sleepId, Long userId) {
		Optional<Sleep> sleepOpt = sleepRepository.findById(sleepId);

		if (sleepOpt.isPresent()) {
			Sleep sleep = sleepOpt.get();

			if (!sleep.getUser().getId().equals(userId)) {
				return Optional.empty();
			}
			return Optional.of(SleepResponse.sleepResponse(sleep));
		}

		return Optional.empty();
	}

	@Transactional
	public SleepResponse createSleepRecord(Long userId, SleepRequest sleepRequest) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User is not found."));
		Sleep sleep = new Sleep();
		sleep.setUser(user);
		sleep.setSleepStartTime(sleepRequest.getSleepStartTime());
		sleep.setSleepEndTime(sleepRequest.getSleepEndTime());
		sleep.setQualityRating(sleepRequest.getQualityRating());
		sleep.setNotes(sleepRequest.getNotes());

		Sleep sleepRecord = sleepRepository.save(sleep);
		return SleepResponse.sleepResponse(sleepRecord);
	}

	@Transactional
	public Optional<SleepResponse> updateSleepRecord(Long userId, Long sleepId, SleepRequest request) {
		Optional<Sleep> sleepOptional = sleepRepository.findById(sleepId);

		if (sleepOptional.isPresent()) {
			Sleep sleep = sleepOptional.get();

			if (!sleep.getUser().getId().equals(userId)) {
				return Optional.empty();
			}

			sleep.setSleepStartTime(request.getSleepStartTime());
			sleep.setSleepEndTime(request.getSleepEndTime());
			sleep.setQualityRating(request.getQualityRating());
			sleep.setNotes(request.getNotes());

			Sleep sleepRecord = sleepRepository.save(sleep);
			return Optional.of(SleepResponse.sleepResponse(sleepRecord));
		}

		return Optional.empty();
	}

	@Transactional
	public boolean deleteSleepRecord(Long sleepId, Long userId) {
		Optional<Sleep> sleepOptional = sleepRepository.findById(sleepId);

		if (sleepOptional.isPresent()) {
			Sleep sleep = sleepOptional.get();

			if (!sleep.getUser().getId().equals(userId)) {
				return false;
			}

			sleepRepository.delete(sleep);
			return true;
		}
		return false;
	}

}
