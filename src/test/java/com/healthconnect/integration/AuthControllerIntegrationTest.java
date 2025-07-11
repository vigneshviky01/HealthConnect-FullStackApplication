package com.healthconnect.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.entity.User;
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
        testUser.setUsername("Auth User");
        testUser.set("Male");
        testUser.set(30);
        testUser.setWeight(75.0);
        testUser.setHeight(180.0);
        userRepository.save(testUser);
    }

    @Test
    void testSignupSuccess() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("Password123");
        request.setName("New User");
        request.setGender("Female");
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
    void testSignupWithExistingUsername() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("authuser"); // Already exists
        request.setEmail("different@example.com");
        request.setPassword("Password123");
        request.setName("Duplicate User");
        request.setGender("Male");
        request.setAge(35);
        request.setWeight(80.0);
        request.setHeight(175.0);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));
    }

    @Test
    void testSignupWithExistingEmail() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("differentuser");
        request.setEmail("auth@example.com"); // Already exists
        request.setPassword("Password123");
        request.setName("Duplicate Email User");
        request.setGender("Female");
        request.setAge(28);
        request.setWeight(70.0);
        request.setHeight(170.0);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
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
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("authuser"))
                .andExpect(jsonPath("$.email").value("auth@example.com"));
    }

    @Test
    void testSigninWithEmail() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("auth@example.com"); // Using email instead of username
        request.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.username").value("authuser"));
    }

    @Test
    void testSigninWithInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("authuser");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testSigninWithNonExistentUser() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("nonexistentuser");
        request.setPassword("anypassword");

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogout() throws Exception {
        // First, sign in to get a token
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

        // Then, test logout with the token
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You've been signed out!"));
    }

    @Test
    void testSignupValidationErrors() throws Exception {
        SignupRequest request = new SignupRequest();
        // Missing required fields
        request.setUsername("");
        request.setEmail("invalid-email");
        request.setPassword("short");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}