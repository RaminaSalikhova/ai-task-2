package com.example.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.config.TestSecurityConfig;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void register_WithValidData_ShouldReturnAuthResponse() throws Exception {
        // Create registration request
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        // Create expected response
        AuthResponse response = new AuthResponse("jwt-token", request.getEmail(), request.getName());

        // Mock service response
        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        // Perform request and verify response
        mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.email").value(request.getEmail()))
            .andExpect(jsonPath("$.name").value(request.getName()));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnAuthResponse() throws Exception {
        // Create login request
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        // Create expected response
        AuthResponse response = new AuthResponse("jwt-token", request.getEmail(), "John Doe");

        // Mock service response
        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        // Perform request and verify response
        mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.email").value(request.getEmail()))
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

} 