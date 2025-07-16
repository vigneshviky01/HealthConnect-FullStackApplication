package com.healthconnect.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.Sleep;
import com.healthconnect.entity.User;
import com.healthconnect.repository.SleepRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.SleepRequest;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SleepControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SleepRepository sleepRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDetailsImpl userDetails;
    private Sleep testSleep;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("sleepuser");
        testUser.setEmail("sleep@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        // Create and save a test sleep record
        testSleep = new Sleep();
        testSleep.setUser(testUser);
        testSleep.setSleepStartTime(LocalDateTime.now().minusHours(8));
        testSleep.setSleepEndTime(LocalDateTime.now());
        testSleep.setQualityRating(4);
        testSleep.setNotes("Good sleep");
        sleepRepository.save(testSleep);

        // Set up authentication
        userDetails = UserDetailsImpl.build(testUser);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testCreateSleepRecord() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().minusHours(7);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        
        SleepRequest request = new SleepRequest();
        request.setSleepStartTime(startTime);
        request.setSleepEndTime(endTime);
        request.setNotes("Excellent sleep");

        mockMvc.perform(post("/api/sleep")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.notes").value("Excellent sleep"));
    }

    @Test
    void testGetSleepRecordById() throws Exception {
        mockMvc.perform(get("/api/sleep/" + testSleep.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testSleep.getId()))
                .andExpect(jsonPath("$.qualityRating").value(4));
    }

    @Test
    void testGetAllSleepRecords() throws Exception {
        mockMvc.perform(get("/api/sleep")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].qualityRating").value(4));
    }

    @Test
    void testGetSleepRecordsByDate() throws Exception {
        mockMvc.perform(get("/api/sleep/by-date")
                .param("date", testSleep.getSleepStartTime().toLocalDate().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateSleepRecord() throws Exception {
        SleepRequest request = new SleepRequest();
        request.setSleepStartTime(testSleep.getSleepStartTime());
        request.setSleepEndTime(testSleep.getSleepEndTime());
        request.setQualityRating(3);
        request.setNotes("Updated sleep quality");

        mockMvc.perform(put("/api/sleep/" + testSleep.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qualityRating").value(3))
                .andExpect(jsonPath("$.notes").value("Updated sleep quality"));
    }

    @Test
    void testDeleteSleepRecord() throws Exception {
        mockMvc.perform(delete("/api/sleep/" + testSleep.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());

        assertThat(sleepRepository.findById(testSleep.getId())).isEmpty();
    }

    // Test for non-existent sleep record
    @Test
    void testGetNonExistentSleepRecord() throws Exception {
        mockMvc.perform(get("/api/sleep/999999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}