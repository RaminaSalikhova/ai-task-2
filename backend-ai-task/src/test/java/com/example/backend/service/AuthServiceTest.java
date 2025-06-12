package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.backend.config.TestSecurityConfig;
import com.example.backend.domain.AuthUser;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.repository.AuthUserRepository;
import com.example.backend.security.JwtTokenProvider;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private AuthUser testUser;

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();

        // Create test user
        testUser = new AuthUser();
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPasswordHash(passwordEncoder.encode("password123"));

        testUser = authUserRepository.save(testUser);

        // Mock JWT token generation
        when(jwtTokenProvider.generateToken(any())).thenReturn("mocked-jwt-token");
    }

    @Test
    void register_WithValidData_ShouldCreateUser() {
        // Create registration request
        RegisterRequest request = new RegisterRequest();
        request.setName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setPassword("password123");

        // Mock authentication with UserDetails as principal
        org.springframework.security.core.userdetails.User userDetails =
            new org.springframework.security.core.userdetails.User(
                request.getEmail(),
                request.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
            userDetails,
            request.getPassword(),
            userDetails.getAuthorities()
        );
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        // Register new user
        AuthResponse response = authService.register(request);

        // Verify response
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getName(), response.getName());

        // Verify user was created in database
        AuthUser savedUser = authUserRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AssertionError("User should be present in database"));

        assertEquals(request.getName(), savedUser.getName());
        assertEquals(request.getEmail(), savedUser.getEmail());
        assertTrue(passwordEncoder.matches(request.getPassword(), savedUser.getPasswordHash()));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        // Create login request
        LoginRequest request = new LoginRequest();
        request.setEmail(testUser.getEmail());
        request.setPassword("password123");

        // Mock authentication with UserDetails as principal
        org.springframework.security.core.userdetails.User userDetails =
            new org.springframework.security.core.userdetails.User(
                testUser.getEmail(),
                testUser.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
            userDetails,
            testUser.getPasswordHash(),
            userDetails.getAuthorities()
        );
        // Only mock authenticationManager, not repository (let it hit the real DB)
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getName(), response.getName());
    }

    @Test
    void login_WithInvalidCredentials_ShouldThrowException() {
        LoginRequest request = new LoginRequest();
        request.setEmail(testUser.getEmail());
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any()))
            .thenThrow(new RuntimeException("Bad credentials"));

        Exception exception = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Bad credentials", exception.getMessage());
    }
}