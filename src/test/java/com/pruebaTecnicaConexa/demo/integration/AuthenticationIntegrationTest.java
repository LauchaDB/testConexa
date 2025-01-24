package com.pruebaTecnicaConexa.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebaTecnicaConexa.demo.controller.AuthController;
import com.pruebaTecnicaConexa.demo.model.auth.LoginRequest;
import com.pruebaTecnicaConexa.demo.model.auth.LoginResponse;
import com.pruebaTecnicaConexa.demo.security.JwtAuthenticationFilter;
import com.pruebaTecnicaConexa.demo.service.AuthenticationService;
import com.pruebaTecnicaConexa.demo.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({AuthController.class, JwtAuthenticationFilter.class, JwtService.class, AuthenticationService.class})
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullAuthenticationFlow() throws Exception {
        // Register a new user
        LoginRequest registerRequest = new LoginRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse registerResponse = objectMapper.readValue(
                registerResult.getResponse().getContentAsString(),
                LoginResponse.class
        );

        assertNotNull(registerResponse.getToken());
        assertEquals("testuser", registerResponse.getUsername());

        // Login with the registered user
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                LoginResponse.class
        );

        assertNotNull(loginResponse.getToken());
        assertEquals("testuser", loginResponse.getUsername());

        // Access protected endpoint with token
        mockMvc.perform(get("/api/v1/films")
                .header("Authorization", "Bearer " + loginResponse.getToken()))
                .andExpect(status().isOk());

        // Access protected endpoint without token should fail
        mockMvc.perform(get("/api/v1/films"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_WithExistingUsername_ShouldFail() throws Exception {
        // Register first user
        LoginRequest firstRequest = new LoginRequest();
        firstRequest.setUsername("existinguser");
        firstRequest.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        // Try to register with same username
        LoginRequest secondRequest = new LoginRequest();
        secondRequest.setUsername("existinguser");
        secondRequest.setPassword("differentpassword");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithInvalidCredentials_ShouldFail() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistentuser");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
} 