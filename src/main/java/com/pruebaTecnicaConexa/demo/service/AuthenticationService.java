package com.pruebaTecnicaConexa.demo.service;

import com.pruebaTecnicaConexa.demo.model.auth.LoginRequest;
import com.pruebaTecnicaConexa.demo.model.auth.LoginResponse;
import com.pruebaTecnicaConexa.demo.model.auth.User;
import com.pruebaTecnicaConexa.demo.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationFacade authenticationFacade;

    private final Map<String, User> users = new HashMap<>();

    public LoginResponse register(LoginRequest request) {
        if (authenticationFacade.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        
        authenticationFacade.saveUser(user);

        String jwt = jwtService.generateToken(authenticationFacade.createUserDetails(user));
        return LoginResponse.builder()
                .token(jwt)
                .username(user.getUsername())
                .build();
    }

    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        User user = authenticationFacade.findByUsername(request.getUsername());
        String jwt = jwtService.generateToken(authenticationFacade.createUserDetails(user));
        
        return LoginResponse.builder()
                .token(jwt)
                .username(user.getUsername())
                .build();
    }
} 