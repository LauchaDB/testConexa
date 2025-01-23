package com.pruebaTecnicaConexa.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebaTecnicaConexa.demo.model.auth.LoginRequest;
import com.pruebaTecnicaConexa.demo.model.auth.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StarWarsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        // Register and login to get auth token
        LoginRequest request = new LoginRequest();
        request.setUsername("swtest");
        request.setPassword("password123");

        // Register
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Login
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                LoginResponse.class
        );

        authToken = loginResponse.getToken();
    }

    @Test
    void whenGetPeople_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").exists());
    }

    @Test
    void whenGetPeopleById_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/people/1")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void whenSearchPeopleByName_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/people/search")
                .param("name", "Luke")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").exists());
    }

    @Test
    void whenGetFilms_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/films")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").exists());
    }

    @Test
    void whenAccessEndpointWithoutToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/people"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAccessEndpointWithInvalidToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/people")
                .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }
} 