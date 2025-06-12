package com.example.backend.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "street")),
        @AttributeOverride(name = "suite", column = @Column(name = "suite")),
        @AttributeOverride(name = "city", column = @Column(name = "city")),
        @AttributeOverride(name = "zipcode", column = @Column(name = "zipcode"))
    })
    private Address address;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "company_name", insertable = false, updatable = false)),
        @AttributeOverride(name = "catchPhrase", column = @Column(name = "company_catch_phrase")),
        @AttributeOverride(name = "bs", column = @Column(name = "company_bs"))
    })
    private Company company;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;
}