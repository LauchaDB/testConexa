package com.pruebaTecnicaConexa.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();
    }

    @Test
    void whenGenerateToken_thenTokenIsValid() {
        String token = jwtService.generateToken(userDetails);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void whenExtractUsername_thenReturnsCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        
        assertEquals("testuser", username);
    }

    @Test
    void whenTokenIsExpired_thenValidationFails() {
        // Set a very short expiration time
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        
        String token = jwtService.generateToken(userDetails);
        assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void whenUserDetailsDoNotMatch_thenValidationFails() {
        String token = jwtService.generateToken(userDetails);
        
        UserDetails differentUser = User.builder()
                .username("different")
                .password("password")
                .authorities(new ArrayList<>())
                .build();
        
        assertFalse(jwtService.isTokenValid(token, differentUser));
    }
} 