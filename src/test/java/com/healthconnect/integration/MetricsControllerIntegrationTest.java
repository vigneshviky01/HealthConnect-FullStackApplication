package com.healthconnect.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.*;
import com.healthconnect.repository.*;
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
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MetricsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SleepRepository sleepRepository;

    @Autowired
    private WaterIntakeRepository waterIntakeRepository;

    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("metricsuser");
        testUser.setEmail("metrics@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        // Create test data for metrics
        createTestActivity();
        createTestSleep();
        createTestWaterIntake();
        createTestMood();

        // Set up authentication
        userDetails = UserDetailsImpl.build(testUser);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void createTestActivity() {
        // Create activity records for the past week
        for (int i = 0; i < 7; i++) {
            Activity activity = new Activity();
            activity.setUser(testUser);
            activity.setActivityDate(LocalDate.now().minusDays(i));
            activity.setStepsCount(8000 + (i * 500));
            activity.setCaloriesBurned(300 + (i * 20));
            activity.setWorkoutType("Running");
            activity.setWorkoutDurationMinutes(30 + (i * 5));
            activity.setDistanceKm(3.0 + (i * 0.5));
            activityRepository.save(activity);
        }
    }

    private void createTestSleep() {
        // Create sleep records for the past week
        for (int i = 0; i < 7; i++) {
            Sleep sleep = new Sleep();
            sleep.setUser(testUser);
            sleep.setSleepStartTime(LocalDateTime.now().minusDays(i + 1).withHour(22).withMinute(0));
            sleep.setSleepEndTime(LocalDateTime.now().minusDays(i).withHour(6).withMinute(0));
            sleep.setQualityRating(3 + (i % 3));
            sleepRepository.save(sleep);
        }
    }

    private void createTestWaterIntake() {
        // Create water intake records for the past week
        for (int i = 0; i < 7; i++) {
            WaterIntake waterIntake = new WaterIntake();
            waterIntake.setUser(testUser);
            waterIntake.setIntakeDate(LocalDate.now().minusDays(i));
            waterIntake.setAmountLiters(2.0 + (i * 0.2));
            waterIntakeRepository.save(waterIntake);
        }
    }

    private void createTestMood() {
        // Create mood records for the past week
        for (int i = 0; i < 7; i++) {
            Mood mood = new Mood();
            mood.setUser(testUser);
            mood.setMoodDate(LocalDate.now().minusDays(i));
            mood.setMoodRating(3 + (i % 3));
            moodRepository.save(mood);
        }
    }

    @Test
    void testGetWeeklyMetrics() throws Exception {
        mockMvc.perform(get("/api/metrics/weekly")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeFrame").value("WEEKLY"))
                .andExpect(jsonPath("$.avgStepsCount").isNumber())
                .andExpect(jsonPath("$.avgCaloriesBurned").isNumber())
                .andExpect(jsonPath("$.avgDistanceKm").isNumber())
                .andExpect(jsonPath("$.avgWorkoutMinutes").isNumber())
                .andExpect(jsonPath("$.avgSleepHours").isNumber())
                .andExpect(jsonPath("$.avgSleepQuality").isNumber())
                .andExpect(jsonPath("$.avgWaterIntakeLiters").isNumber())
                .andExpect(jsonPath("$.avgMoodRating").isNumber());
    }

    @Test
    void testGetMonthlyMetrics() throws Exception {
        mockMvc.perform(get("/api/metrics/monthly")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeFrame").value("MONTHLY"))
                .andExpect(jsonPath("$.avgStepsCount").isNumber())
                .andExpect(jsonPath("$.avgCaloriesBurned").isNumber())
                .andExpect(jsonPath("$.avgDistanceKm").isNumber())
                .andExpect(jsonPath("$.avgWorkoutMinutes").isNumber())
                .andExpect(jsonPath("$.avgSleepHours").isNumber())
                .andExpect(jsonPath("$.avgSleepQuality").isNumber())
                .andExpect(jsonPath("$.avgWaterIntakeLiters").isNumber())
                .andExpect(jsonPath("$.avgMoodRating").isNumber());
    }

    @Test
    void testGetCustomRangeMetrics() throws Exception {
        String startDate = LocalDate.now().minusDays(5).toString();
        String endDate = LocalDate.now().toString();

        mockMvc.perform(get("/api/metrics/custom")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeFrame").value("CUSTOM"))
                .andExpect(jsonPath("$.startDate").value(startDate))
                .andExpect(jsonPath("$.endDate").value(endDate))
                .andExpect(jsonPath("$.avgStepsCount").isNumber())
                .andExpect(jsonPath("$.avgCaloriesBurned").isNumber())
                .andExpect(jsonPath("$.avgDistanceKm").isNumber())
                .andExpect(jsonPath("$.avgWorkoutMinutes").isNumber())
                .andExpect(jsonPath("$.avgSleepHours").isNumber())
                .andExpect(jsonPath("$.avgSleepQuality").isNumber())
                .andExpect(jsonPath("$.avgWaterIntakeLiters").isNumber())
                .andExpect(jsonPath("$.avgMoodRating").isNumber());
    }

    @Test
    void testGetCustomRangeMetricsWithInvalidDateRange() throws Exception {
        String startDate = LocalDate.now().toString();
        String endDate = LocalDate.now().minusDays(5).toString(); // End date before start date

        mockMvc.perform(get("/api/metrics/custom")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCustomRangeMetricsWithMissingParameters() throws Exception {
        mockMvc.perform(get("/api/metrics/custom")
                .param("startDate", LocalDate.now().minusDays(5).toString())
                // Missing endDate parameter
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}