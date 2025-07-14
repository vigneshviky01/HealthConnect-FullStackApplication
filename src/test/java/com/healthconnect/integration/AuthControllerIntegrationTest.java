package com.healthconnect.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.entity.User;
import com.healthconnect.entity.UserProfile;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.transfer.request.LoginRequest;
import com.healthconnect.transfer.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private final String TEST_PASSWORD = "testPassword123";

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("authuser");
        testUser.setEmail("auth@example.com");
        testUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        userRepository.save(testUser);
    }

    @Test
    void testSignupSuccess() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("Password123");
        request.setName("New User");
        request.setGender(UserProfile.Gender.FEMALE);
        request.setAge(25);
        request.setWeight(65.0);
        request.setHeight(165.0);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Verify user was created in the database
        User newUser = userRepository.findByUsername("newuser").orElse(null);
        assertThat(newUser).isNotNull();
        assertThat(newUser.getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    void testSignupWithExistingUsernameOrEmail() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("authuser"); // Already exists
        request.setEmail("auth@example.com"); // Already exists
        request.setPassword("Password123");
        request.setName("Duplicate User");
        request.setGender(UserProfile.Gender.MALE);
        request.setAge(35);
        request.setWeight(80.0);
        request.setHeight(175.0);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSigninSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("authuser");
        request.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.username").value("authuser"));
    }

    @Test
    void testSigninFailure() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("authuser");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogout() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("authuser");
        loginRequest.setPassword(TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("token").asText();

        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully!"));
    }
}