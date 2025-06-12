package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private GeoDto geo;
} 