package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private AddressDto address;
    private String phone;
    private String website;
    private CompanyDto company;
} 