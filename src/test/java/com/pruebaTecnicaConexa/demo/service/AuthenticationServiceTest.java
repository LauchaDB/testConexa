package com.pruebaTecnicaConexa.demo.service;

import com.pruebaTecnicaConexa.demo.model.auth.LoginRequest;
import com.pruebaTecnicaConexa.demo.model.auth.LoginResponse;
import com.pruebaTecnicaConexa.demo.model.auth.User;
import com.pruebaTecnicaConexa.demo.security.AuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest;
    private final String TEST_TOKEN = "test.jwt.token";
    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);
    }

    @Test
    void register_WithNewUser_ShouldReturnLoginResponse() {
        // Arrange
        when(authenticationFacade.findByUsername(TEST_USERNAME)).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(TEST_TOKEN);
        
        User expectedUser = User.builder()
                .username(TEST_USERNAME)
                .password("encodedPassword")
                .role("USER")
                .build();
        when(authenticationFacade.createUserDetails(any(User.class))).thenReturn(mock(UserDetails.class));

        // Act
        LoginResponse response = authenticationService.register(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(TEST_USERNAME, response.getUsername());
        verify(authenticationFacade).saveUser(any(User.class));
    }

    @Test
    void register_WithExistingUsername_ShouldThrowException() {
        // Arrange
        when(authenticationFacade.findByUsername(TEST_USERNAME)).thenReturn(mock(User.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(loginRequest));
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnLoginResponse() {
        // Arrange
        User mockUser = User.builder()
                .username(TEST_USERNAME)
                .password("encodedPassword")
                .role("USER")
                .build();
        
        when(authenticationFacade.findByUsername(TEST_USERNAME)).thenReturn(mockUser);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(TEST_TOKEN);
        when(authenticationFacade.createUserDetails(any(User.class))).thenReturn(mock(UserDetails.class));

        // Act
        LoginResponse response = authenticationService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(TEST_USERNAME, response.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_WithInvalidCredentials_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(loginRequest));
    }
} 