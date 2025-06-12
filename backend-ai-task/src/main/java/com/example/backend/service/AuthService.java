package com.example.backend.service;

import com.example.backend.domain.AuthUser;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.repository.AuthUserRepository;
import com.example.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        AuthUser authUser = new AuthUser();
        authUser.setName(request.getName());
        authUser.setEmail(request.getEmail());
        authUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        authUserRepository.save(authUser);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = tokenProvider.generateToken(userDetails);
        return new AuthResponse(token, authUser.getEmail(), authUser.getName());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = tokenProvider.generateToken(userDetails);

        AuthUser authUser = authUserRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(token, authUser.getEmail(), authUser.getName());
    }
} 