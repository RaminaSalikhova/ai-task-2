package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "auth_users")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String passwordHash;
} 