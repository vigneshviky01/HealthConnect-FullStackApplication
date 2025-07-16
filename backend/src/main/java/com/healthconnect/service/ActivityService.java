package com.healthconnect.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthconnect.entity.Activity;
import com.healthconnect.entity.User;
import com.healthconnect.repository.ActivityRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.ActivityRequest;
import com.healthconnect.transfer.response.ActivityResponse;

import jakarta.transaction.Transactional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<ActivityResponse> getAllActivities(Long userId, LocalDate startDate, LocalDate endDate, String sort) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Activity> activities;
        
        if (startDate != null && endDate != null) {
            activities = activityRepository.findByUserAndActivityDateBetween(user, startDate, endDate);
        } else {
            activities = activityRepository.findByUser(user);
        }
        
        // Sort by activity date
        String direction = (sort == null || !sort.equalsIgnoreCase("asc")) ? "desc" : "asc";
        activities.sort((a, b) -> {
            int compare = a.getActivityDate().compareTo(b.getActivityDate());
            return direction.equals("asc") ? compare : -compare;
        });
        
        return activities.stream()
                .map(ActivityResponse::fromActivity)
                .collect(Collectors.toList());
    }
    
//    // Flexible filter method for activities
//    public List<ActivityResponse> getActivitiesByFilter(Long userId, LocalDate startDate, LocalDate endDate, String workoutType, Integer minSteps, Integer minCalories, Double minDistance, Integer minDuration, String sort) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        List<Activity> activities = new ArrayList<>(activityRepository.findByUser(user)); 
//        activities = activities.stream()
//                .filter(a -> (startDate == null || !a.getActivityDate().isBefore(startDate)))
//                .filter(a -> (endDate == null || !a.getActivityDate().isAfter(endDate)))
//                .filter(a -> (workoutType == null || a.getWorkoutType().equalsIgnoreCase(workoutType)))
//                .filter(a -> (minSteps == null || (a.getStepsCount() != null && a.getStepsCount() >= minSteps)))
//                .filter(a -> (minCalories == null || (a.getCaloriesBurned() != null && a.getCaloriesBurned() >= minCalories)))
//                .filter(a -> (minDistance == null || (a.getDistanceKm() != null && a.getDistanceKm() >= minDistance)))
//                .filter(a -> (minDuration == null || (a.getWorkoutDurationMinutes() != null && a.getWorkoutDurationMinutes() >= minDuration)))
//                .toList();
//        activities = new ArrayList<>(activities); 
//        String direction = (sort == null || !sort.equalsIgnoreCase("asc")) ? "desc" : "asc";
//        activities.sort((a, b) -> {
//            int compare = a.getActivityDate().compareTo(b.getActivityDate());
//            return direction.equals("asc") ? compare : -compare;
//        });
//        return activities.stream().map(ActivityResponse::fromActivity).collect(Collectors.toList());
//    }
    
    // Get a specific activity by ID
    public Optional<ActivityResponse> getActivityById(Long activityId, Long userId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Verify the activity belongs to the user
            if (!activity.getUser().getId().equals(userId)) {
                return Optional.empty();
            }
            
            return Optional.of(ActivityResponse.fromActivity(activity));
        }
        
        return Optional.empty();
    }
    
    //Create a new activity record
    @Transactional
    public ActivityResponse createActivity(Long userId, ActivityRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Activity activity = new Activity();
        activity.setUser(user);
        updateActivityFromRequest(activity, request);
        
        Activity savedActivity = activityRepository.save(activity);
        return ActivityResponse.fromActivity(savedActivity);
    }
    
    //Update an existing activity record
    @Transactional
    public Optional<ActivityResponse> updateActivity(Long userId, Long activityId, ActivityRequest request) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Verify the activity belongs to the user
            if (!activity.getUser().getId().equals(userId)) {
                return Optional.empty();
            }
            
            updateActivityFromRequest(activity, request);
            Activity updatedActivity = activityRepository.save(activity);
            return Optional.of(ActivityResponse.fromActivity(updatedActivity));
        }
        
        return Optional.empty();
    }
    
    // Delete an activity record
    @Transactional
    public boolean deleteActivity(Long activityId, Long userId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Verify the activity belongs to the user
            if (!activity.getUser().getId().equals(userId)) {
                return false;
            }
            
            activityRepository.delete(activity);
            return true;
        }
        
        return false;
    }
    
    // Helper method to update activity fields from request
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