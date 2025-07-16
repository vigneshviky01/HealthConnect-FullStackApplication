package com.healthconnect.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.User;
import com.healthconnect.entity.WaterIntake;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.repository.WaterIntakeRepository;
import com.healthconnect.transfer.request.WaterIntakeRequest;
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
class WaterIntakeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WaterIntakeRepository waterIntakeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDetailsImpl userDetails;
    private WaterIntake testWaterIntake;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("wateruser");
        testUser.setEmail("water@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        // Create and save a test water intake record
        testWaterIntake = new WaterIntake();
        testWaterIntake.setUser(testUser);
        testWaterIntake.setIntakeDate(LocalDate.now());
        testWaterIntake.setAmountLiters(2.5);
        waterIntakeRepository.save(testWaterIntake);

        // Set up authentication
        userDetails = UserDetailsImpl.build(testUser);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testCreateWaterIntake() throws Exception {
        WaterIntakeRequest request = new WaterIntakeRequest();
        request.setIntakeDate(LocalDate.now().minusDays(1));
        request.setAmountLiters(3.0);

        mockMvc.perform(post("/api/water")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amountLiters").value(3.0));
    }

    @Test
    void testGetWaterIntakeById() throws Exception {
        mockMvc.perform(get("/api/water/" + testWaterIntake.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testWaterIntake.getId()))
                .andExpect(jsonPath("$.amountLiters").value(2.5));
    }

    @Test
    void testGetAllWaterIntakes() throws Exception {
        mockMvc.perform(get("/api/water")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amountLiters").value(2.5));
    }

    @Test
    void testGetWaterIntakesByDateRange() throws Exception {
        mockMvc.perform(get("/api/water")
                .param("startDate", LocalDate.now().minusDays(7).toString())
                .param("endDate", LocalDate.now().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amountLiters").value(2.5));
    }

    @Test
    void testUpdateWaterIntake() throws Exception {
        WaterIntakeRequest request = new WaterIntakeRequest();
        request.setIntakeDate(testWaterIntake.getIntakeDate());
        request.setAmountLiters(3.5);

        mockMvc.perform(put("/api/water/" + testWaterIntake.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amountLiters").value(3.5));
    }

    @Test
    void testDeleteWaterIntake() throws Exception {
        mockMvc.perform(delete("/api/water/" + testWaterIntake.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Water intake record deleted successfully"));

        assertThat(waterIntakeRepository.findById(testWaterIntake.getId())).isEmpty();
    }

    // Test for non-existent water intake record
    @Test
    void testGetNonExistentWaterIntake() throws Exception {
        mockMvc.perform(get("/api/water/999999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test validation error
    @Test
    void testCreateWaterIntakeWithNegativeAmount() throws Exception {
        WaterIntakeRequest request = new WaterIntakeRequest();
        request.setIntakeDate(LocalDate.now());
        request.setAmountLiters(-1.0); // Invalid: should be non-negative

        mockMvc.perform(post("/api/water")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}