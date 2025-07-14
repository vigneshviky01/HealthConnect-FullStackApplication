package com.healthconnect.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.config.service.UserDetailsImpl;
import com.healthconnect.entity.User;
import com.healthconnect.entity.UserProfile;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.ProfileUpdateRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.healthconnect.repository.UserProfileRepository userProfileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        // Create and save a real User entity
        testUser = new User();
        testUser.setUsername("integrationuser");
        testUser.setEmail("integration@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        // Create and save a UserProfile for the test user
        UserProfile profile = new UserProfile();
        profile.setUser(testUser);
        profile.setName("Initial Name");
        profile.setGender(UserProfile.Gender.MALE);
        profile.setAge(25);
        profile.setWeight(65.0);
        profile.setHeight(170.0);
        userProfileRepository.save(profile);

        // Build UserDetailsImpl
        userDetails = UserDetailsImpl.build(testUser);

        // Set authenticated user in SecurityContextHolder
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testGetUserProfile() throws Exception {
        mockMvc.perform(get("/api/users/profile")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    void testUpdateUserProfile() throws Exception {
        ProfileUpdateRequest req = new ProfileUpdateRequest();
        req.setFullName("Updated Name");
        req.setGender(UserProfile.Gender.OTHER);
        req.setAge(30);
        req.setWeight(70.0);
        req.setHeight(175.0);
        // All fields are set and valid
        mockMvc.perform(put("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testDeleteUserAccount() throws Exception {
        mockMvc.perform(delete("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Your Account Deleted Successfully."));
    }

    // Helper method to convert object to JSON string
    private String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}