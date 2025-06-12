package com.example.backend.service;

import com.example.backend.config.TestSecurityConfig;
import com.example.backend.domain.Address;
import com.example.backend.domain.Company;
import com.example.backend.domain.Geo;
import com.example.backend.domain.User;
import com.example.backend.dto.UserDto;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
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

        testUser = userRepository.save(testUser);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Create another user
        User anotherUser = new User();
        anotherUser.setName("Jane Doe");
        anotherUser.setUsername("janedoe");
        anotherUser.setEmail("jane@example.com");
        anotherUser.setPhone("987-654-3210");
        anotherUser.setWebsite("www.jane.com");
        userRepository.save(anotherUser);

        List<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Doe", users.get(1).getName());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        UserDto user = userService.getUserById(testUser.getId());

        assertNotNull(user);
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getPhone(), user.getPhone());
        assertEquals(testUser.getWebsite(), user.getWebsite());
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        Long nonExistentId = 999L;
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.getUserById(nonExistentId)
        );
        assertEquals("User not found with id : '999'", exception.getMessage());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        UserDto newUserDto = new UserDto();
        newUserDto.setName("New User");
        newUserDto.setUsername("newuser");
        newUserDto.setEmail("new@example.com");
        newUserDto.setPhone("555-555-5555");
        newUserDto.setWebsite("www.newuser.com");

        UserDto createdUser = userService.createUser(newUserDto);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(newUserDto.getName(), createdUser.getName());
        assertEquals(newUserDto.getEmail(), createdUser.getEmail());
        assertEquals(newUserDto.getPhone(), createdUser.getPhone());
        assertEquals(newUserDto.getWebsite(), createdUser.getWebsite());
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated User");
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updated@example.com");
        updateDto.setPhone("999-999-9999");
        updateDto.setWebsite("www.updated.com");

        UserDto updatedUser = userService.updateUser(testUser.getId(), updateDto);

        assertNotNull(updatedUser);
        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals(updateDto.getName(), updatedUser.getName());
        assertEquals(updateDto.getEmail(), updatedUser.getEmail());
        assertEquals(updateDto.getPhone(), updatedUser.getPhone());
        assertEquals(updateDto.getWebsite(), updatedUser.getWebsite());
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        Long nonExistentId = 999L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated User");
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updated@example.com");

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.updateUser(nonExistentId, updateDto)
        );
        assertEquals("User not found with id : '999'", exception.getMessage());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        userService.deleteUser(testUser.getId());
        assertFalse(userRepository.existsById(testUser.getId()));
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        Long nonExistentId = 999L;
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.deleteUser(nonExistentId)
        );
        assertEquals("User not found with id : '999'", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithFullDetails() {
        // Create test data
        UserDto newUserDto = new UserDto();
        newUserDto.setName("Jane Doe");
        newUserDto.setUsername("janedoe");
        newUserDto.setEmail("jane@example.com");
        newUserDto.setPhone("987-654-3210");
        newUserDto.setWebsite("www.jane.com");

        // Create and save the user
        UserDto createdUser = userService.createUser(newUserDto);

        // Verify the user was created correctly
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(newUserDto.getName(), createdUser.getName());
        assertEquals(newUserDto.getEmail(), createdUser.getEmail());
        assertEquals(newUserDto.getPhone(), createdUser.getPhone());
        assertEquals(newUserDto.getWebsite(), createdUser.getWebsite());

        // Verify the user exists in the database
        User savedUser = userRepository.findById(createdUser.getId()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(newUserDto.getName(), savedUser.getName());
        assertEquals(newUserDto.getEmail(), savedUser.getEmail());
    }
} 