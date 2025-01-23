package com.pruebaTecnicaConexa.demo.service;

import com.pruebaTecnicaConexa.demo.model.auth.LoginRequest;
import com.pruebaTecnicaConexa.demo.model.auth.LoginResponse;
import com.pruebaTecnicaConexa.demo.model.auth.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFacadeTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest;
    private final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
    }

    @Test
    void whenRegister_thenReturnLoginResponse() {
        // Arrange
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(TEST_TOKEN);

        // Act
        LoginResponse response = authenticationService.register(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(loginRequest.getUsername(), response.getUsername());
        verify(passwordEncoder).encode(loginRequest.getPassword());
        verify(jwtService).generateToken(any(UserDetails.class));
    }

    @Test
    void whenAuthenticate_thenReturnLoginResponse() {
        // Arrange
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(TEST_TOKEN);
        
        // Register a user first
        authenticationService.register(loginRequest);

        // Act
        LoginResponse response = authenticationService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(loginRequest.getUsername(), response.getUsername());
        verify(authenticationManager).authenticate(
            any(UsernamePasswordAuthenticationToken.class)
        );
    }
} 