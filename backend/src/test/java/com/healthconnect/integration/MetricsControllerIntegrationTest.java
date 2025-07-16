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

    private User testUser;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("metricsuser");
        testUser.setEmail("metrics@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

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
        // Create activity records for the past 28 days
        for (int i = 0; i < 28; i++) {
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
        // Create sleep records for the past 28 days
        for (int i = 0; i < 28; i++) {
            Sleep sleep = new Sleep();
            sleep.setUser(testUser);
            sleep.setSleepStartTime(LocalDateTime.now().minusDays(i + 1).withHour(22).withMinute(0));
            sleep.setSleepEndTime(LocalDateTime.now().minusDays(i).withHour(6).withMinute(0));
            sleep.setQualityRating(3 + (i % 3));
            sleepRepository.save(sleep);
        }
    }

    private void createTestWaterIntake() {
        // Create water intake records for the past 28 days
        for (int i = 0; i < 28; i++) {
            WaterIntake waterIntake = new WaterIntake();
            waterIntake.setUser(testUser);
            waterIntake.setIntakeDate(LocalDate.now().minusDays(i));
            waterIntake.setAmountLiters(2.0 + (i * 0.2));
            waterIntakeRepository.save(waterIntake);
        }
    }

    private void createTestMood() {
        // Create mood records for the past 28 days
        for (int i = 0; i < 28; i++) {
            Mood mood = new Mood();
            mood.setUser(testUser);
            mood.setMoodDate(LocalDate.now().minusDays(i));
            mood.setMoodRating(3 + (i % 3));
            moodRepository.save(mood);
        }
    }

    @Test
    void testGetFourWeekMetrics_Default() throws Exception {
        mockMvc.perform(get("/api/metrics/monthly/graph")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].totalSteps").exists())
                .andExpect(jsonPath("$[0].totalCalories").exists())
                .andExpect(jsonPath("$[0].totalDistance").exists())
                .andExpect(jsonPath("$[0].totalWorkoutMinutes").exists())
                .andExpect(jsonPath("$[0].totalSleepHours").exists())
                .andExpect(jsonPath("$[0].avgSleepQuality").exists())
                .andExpect(jsonPath("$[0].totalWaterIntake").exists())
                .andExpect(jsonPath("$[0].avgMoodRating").exists());
    }

    @Test
    void testGetFourWeekMetrics_YearMonth() throws Exception {
        String yearMonth = LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue());
        mockMvc.perform(get("/api/metrics/monthly/graph")
                .param("yearMonth", yearMonth)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists());
    }

    @Test
    void testGetFourWeekMetrics_MetricsFilter() throws Exception {
        mockMvc.perform(get("/api/metrics/monthly/graph")
                .param("metrics", "totalSteps,avgMoodRating")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].totalSteps").exists())
                .andExpect(jsonPath("$[0].avgMoodRating").exists())
                .andExpect(jsonPath("$[0].totalCalories").doesNotExist())
                .andExpect(jsonPath("$[0].totalDistance").doesNotExist());
    }

    @Test
    void testGetFourWeekMetrics_YearMonthAndMetrics() throws Exception {
        String yearMonth = LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue());
        mockMvc.perform(get("/api/metrics/monthly/graph")
                .param("yearMonth", yearMonth)
                .param("metrics", "totalSleepHours,avgSleepQuality")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].totalSleepHours").exists())
                .andExpect(jsonPath("$[0].avgSleepQuality").exists())
                .andExpect(jsonPath("$[0].totalSteps").doesNotExist());
    }
}