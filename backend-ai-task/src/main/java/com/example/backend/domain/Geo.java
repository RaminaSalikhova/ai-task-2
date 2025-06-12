package com.example.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Geo {
    @Column(name = "geo_lat")
    private String lat;
    @Column(name = "geo_lng")
    private String lng;
} 