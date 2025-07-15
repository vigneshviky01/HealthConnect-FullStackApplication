package com.healthconnect.service;

// Service layer for handling activity-related business logic
import com.healthconnect.entity.Activity;
import com.healthconnect.entity.User;
import com.healthconnect.repository.ActivityRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.ActivityRequest;
import com.healthconnect.transfer.response.ActivityResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Marks this as a Spring service
@Service
public class ActivityService {

    // Injects ActivityRepository for database operations
    @Autowired
    private ActivityRepository activityRepository;

    // Injects UserRepository to fetch user details
    @Autowired
    private UserRepository userRepository;

    // Retrieves all activities with optional filters for date, workout type, and sort order
    public List<ActivityResponse> getAllActivities(Long userId, LocalDate startDate, LocalDate endDate, String workoutType, String sort) {
        // Validate and fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Activity> activities;

        // Apply filters based on provided parameters
        if (startDate != null && endDate != null && workoutType != null) {
            activities = activityRepository.findByUserAndActivityDateBetweenAndWorkoutType(user, startDate, endDate, workoutType);
        } else if (startDate != null && endDate != null) {
            activities = activityRepository.findByUserAndActivityDateBetween(user, startDate, endDate);
        } else if (workoutType != null) {
            activities = activityRepository.findByUserAndWorkoutType(user, workoutType);
        } else {
            activities = activityRepository.findByUser(user);
        }

        // Sort activities by date
        String direction = (sort == null || !sort.equalsIgnoreCase("asc")) ? "desc" : "asc";
        activities.sort((a, b) -> {
            int compare = a.getActivityDate().compareTo(b.getActivityDate());
            return direction.equals("asc") ? compare : -compare;
        });

        // Convert to response objects
        return activities.stream()
                .map(ActivityResponse::fromActivity)
                .collect(Collectors.toList());
    }

    // Retrieves activities for a specific date
    public List<ActivityResponse> getActivitiesByDate(Long userId, LocalDate date, String sort) {
        // Validate and fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch activities for the given date
        List<Activity> activities = activityRepository.findByUserAndActivityDate(user, date);

        // Sort activities by date
        String direction = (sort == null || !sort.equalsIgnoreCase("asc")) ? "desc" : "asc";
        activities.sort((a, b) -> {
            int compare = a.getActivityDate().compareTo(b.getActivityDate());
            return direction.equals("asc") ? compare : -compare;
        });

        // Convert to response objects
        return activities.stream()
                .map(ActivityResponse::fromActivity)
                .collect(Collectors.toList());
    }

    // Retrieves a specific activity by ID
    public Optional<ActivityResponse> getActivityById(Long activityId, Long userId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);

        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();

            // Verify activity ownership
            if (!activity.getUser().getId().equals(userId)) {
                return Optional.empty();
            }

            return Optional.of(ActivityResponse.fromActivity(activity));
        }

        return Optional.empty();
    }

    // Creates a new activity record
    @Transactional // Ensures database operations are atomic
    public ActivityResponse createActivity(Long userId, ActivityRequest request) {
        // Validate and fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and populate new activity
        Activity activity = new Activity();
        activity.setUser(user);
        updateActivityFromRequest(activity, request);

        // Save to database
        Activity savedActivity = activityRepository.save(activity);
        return ActivityResponse.fromActivity(savedActivity);
    }

    // Updates an existing activity record
    @Transactional
    public Optional<ActivityResponse> updateActivity(Long userId, Long activityId, ActivityRequest request) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);

        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();

            // Verify activity ownership
            if (!activity.getUser().getId().equals(userId)) {
                return Optional.empty();
            }

            // Update activity fields
            updateActivityFromRequest(activity, request);
            Activity updatedActivity = activityRepository.save(activity);
            return Optional.of(ActivityResponse.fromActivity(updatedActivity));
        }

        return Optional.empty();
    }

    // Deletes an activity record
    @Transactional
    public boolean deleteActivity(Long activityId, Long userId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);

        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();

            // Verify activity ownership
            if (!activity.getUser().getId().equals(userId)) {
                return false;
            }

            // Delete from database
            activityRepository.delete(activity);
            return true;
        }

        return false;
    }

    // Updates activity fields from request DTO
    private void updateActivityFromRequest(Activity activity, ActivityRequest request) {
        activity.setActivityDate(request.getActivityDate());
        activity.setStepsCount(request.getStepsCount());
        activity.setCaloriesBurned(request.getCaloriesBurned());
        activity.setWorkoutType(request.getWorkoutType());
        activity.setWorkoutDurationMinutes(request.getWorkoutDurationMinutes());
        activity.setDistanceKm(request.getDistanceKm());
        activity.setNotes(request.getNotes());
    }
}
