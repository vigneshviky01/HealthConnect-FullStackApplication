package com.healthconnect.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.Mood;
import com.healthconnect.entity.User;
import com.healthconnect.repository.MoodRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.MoodRequest;
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
class MoodControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDetailsImpl userDetails;
    private Mood testMood;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("mooduser");
        testUser.setEmail("mood@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        // Create and save a test mood record
        testMood = new Mood();
        testMood.setUser(testUser);
        testMood.setMoodDate(LocalDate.now());
        testMood.setMoodRating(4);
        testMood.setNotes("Feeling good today");
        moodRepository.save(testMood);

        // Set up authentication
        userDetails = UserDetailsImpl.build(testUser);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testCreateMood() throws Exception {
        MoodRequest request = new MoodRequest();
        request.setMoodDate(LocalDate.now().minusDays(1));
        request.setMoodRating(5);
        request.setNotes("Feeling excellent yesterday");

        mockMvc.perform(post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.moodRating").value(5))
                .andExpect(jsonPath("$.notes").value("Feeling excellent yesterday"));
    }

    @Test
    void testGetMoodById() throws Exception {
        mockMvc.perform(get("/api/mood/" + testMood.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testMood.getId()))
                .andExpect(jsonPath("$.moodRating").value(4));
    }

    @Test
    void testGetAllMoods() throws Exception {
        mockMvc.perform(get("/api/mood")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].moodRating").value(4));
    }

    @Test
    void testGetMoodsByDateRange() throws Exception {
        mockMvc.perform(get("/api/mood")
                .param("startDate", LocalDate.now().minusDays(7).toString())
                .param("endDate", LocalDate.now().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].moodRating").value(4));
    }

    @Test
    void testUpdateMood() throws Exception {
        MoodRequest request = new MoodRequest();
        request.setMoodDate(testMood.getMoodDate());
        request.setMoodRating(3);
        request.setNotes("Feeling average now");

        mockMvc.perform(put("/api/mood/" + testMood.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.moodRating").value(3))
                .andExpect(jsonPath("$.notes").value("Feeling average now"));
    }

    @Test
    void testDeleteMood() throws Exception {
        mockMvc.perform(delete("/api/mood/" + testMood.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mood record deleted successfully"));

        assertThat(moodRepository.findById(testMood.getId())).isEmpty();
    }

    // Test for non-existent mood record
    @Test
    void testGetNonExistentMood() throws Exception {
        mockMvc.perform(get("/api/mood/999999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test validation error
    @Test
    void testCreateMoodWithInvalidRating() throws Exception {
        MoodRequest request = new MoodRequest();
        request.setMoodDate(LocalDate.now());
        request.setMoodRating(10); // Invalid: should be 1-5
        request.setNotes("Invalid rating");

        mockMvc.perform(post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}