package com.healthconnect.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.Activity;
import com.healthconnect.entity.User;
import com.healthconnect.repository.ActivityRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.ActivityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ActivityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDetailsImpl userDetails;
    private Activity testActivity;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("activityuser");
        testUser.setEmail("activity@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        // Create and save a test activity
        testActivity = new Activity();
        testActivity.setUser(testUser);
        testActivity.setActivityDate(LocalDate.now());
        testActivity.setStepsCount(10000);
        testActivity.setCaloriesBurned(500);
        testActivity.setWorkoutType("Running");
        testActivity.setWorkoutDurationMinutes(45);
        testActivity.setDistanceKm(5.0);
        testActivity.setNotes("Morning run");
        activityRepository.save(testActivity);

        // Set up authentication
        userDetails = UserDetailsImpl.build(testUser);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testCreateActivity() throws Exception {
        ActivityRequest request = new ActivityRequest();
        request.setActivityDate(LocalDate.now());
        request.setStepsCount(8000);
        request.setCaloriesBurned(400);
        request.setWorkoutType("Cycling");
        request.setWorkoutDurationMinutes(30);
        request.setDistanceKm(10.0);
        request.setNotes("Evening cycling");

        mockMvc.perform(post("/api/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.workoutType").value("Cycling"))
                .andExpect(jsonPath("$.stepsCount").value(8000));
    }

    @Test
    void testGetActivityById() throws Exception {
        mockMvc.perform(get("/api/activities/" + testActivity.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testActivity.getId()))
                .andExpect(jsonPath("$.workoutType").value("Running"));
    }

    @Test
    void testGetAllActivities() throws Exception {
        mockMvc.perform(get("/api/activities")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].workoutType").value("Running"));
    }

    @Test
    void testGetActivitiesByDate() throws Exception {
        mockMvc.perform(get("/api/activities/by-date")
                .param("date", LocalDate.now().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].workoutType").value("Running"));
    }

    @Test
    void testUpdateActivity() throws Exception {
        ActivityRequest request = new ActivityRequest();
        request.setActivityDate(LocalDate.now());
        request.setStepsCount(12000);
        request.setCaloriesBurned(600);
        request.setWorkoutType("Running");
        request.setWorkoutDurationMinutes(60);
        request.setDistanceKm(7.5);
        request.setNotes("Updated run");

        mockMvc.perform(put("/api/activities/" + testActivity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stepsCount").value(12000))
                .andExpect(jsonPath("$.workoutDurationMinutes").value(60));
    }

    @Test
    void testDeleteActivity() throws Exception {
        mockMvc.perform(delete("/api/activities/" + testActivity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Activity record deleted successfully"));

        assertThat(activityRepository.findById(testActivity.getId())).isEmpty();
    }

    // Test for non-existent activity
    @Test
    void testGetNonExistentActivity() throws Exception {
        mockMvc.perform(get("/api/activities/999999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}