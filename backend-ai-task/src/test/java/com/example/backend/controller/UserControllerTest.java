package com.example.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.config.TestSecurityConfig;
import com.example.backend.domain.Address;
import com.example.backend.domain.Company;
import com.example.backend.domain.Geo;
import com.example.backend.domain.User;
import com.example.backend.dto.UserDto;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDto testUserDto;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setUsername("johndoe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("123-456-7890");
        testUser.setWebsite("www.example.com");

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setSuite("Apt 4B");
        address.setCity("New York");
        address.setZipcode("10001");

        Geo geo = new Geo();
        geo.setLat("40.7128");
        geo.setLng("-74.0060");
        address.setGeo(geo);
        testUser.setAddress(address);

        Company company = new Company();
        company.setName("Acme Inc");
        company.setCatchPhrase("Making things better");
        company.setBs("Enterprise solutions");
        testUser.setCompany(company);

        // Create test user DTO
        testUserDto = new UserDto();
        testUserDto.setName(testUser.getName());
        testUserDto.setUsername(testUser.getUsername());
        testUserDto.setEmail(testUser.getEmail());
        testUserDto.setPhone(testUser.getPhone());
        testUserDto.setWebsite(testUser.getWebsite());

        testUser = userRepository.save(testUser);
        testUserId = testUser.getId();
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(testUserDto.getName()))
            .andExpect(jsonPath("$[0].email").value(testUserDto.getEmail()))
            .andExpect(jsonPath("$[0].phone").value(testUserDto.getPhone()))
            .andExpect(jsonPath("$[0].website").value(testUserDto.getWebsite()));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/users/" + testUserId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testUserId))
            .andExpect(jsonPath("$.name").value(testUserDto.getName()))
            .andExpect(jsonPath("$.email").value(testUserDto.getEmail()))
            .andExpect(jsonPath("$.phone").value(testUserDto.getPhone()))
            .andExpect(jsonPath("$.website").value(testUserDto.getWebsite()));
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
        UserDto newUserDto = new UserDto();
        newUserDto.setName("New User");
        newUserDto.setUsername("newuser");
        newUserDto.setEmail("new@example.com");
        newUserDto.setPhone("987-654-3210");
        newUserDto.setWebsite("www.new.com");

        mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newUserDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(newUserDto.getName()))
            .andExpect(jsonPath("$.email").value(newUserDto.getEmail()))
            .andExpect(jsonPath("$.phone").value(newUserDto.getPhone()))
            .andExpect(jsonPath("$.website").value(newUserDto.getWebsite()));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Updated Name");
        updatedUserDto.setUsername(testUserDto.getUsername());
        updatedUserDto.setEmail(testUserDto.getEmail());
        updatedUserDto.setPhone(testUserDto.getPhone());
        updatedUserDto.setWebsite(testUserDto.getWebsite());

        mockMvc.perform(put("/api/users/" + testUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUserDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testUserId))
            .andExpect(jsonPath("$.name").value(updatedUserDto.getName()))
            .andExpect(jsonPath("$.email").value(updatedUserDto.getEmail()));
    }
}