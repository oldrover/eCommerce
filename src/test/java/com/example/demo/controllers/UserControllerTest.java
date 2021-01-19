package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private CreateUserRequest createUserRequest;
    private User user;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("ThisIsHashed");

    }

    @Test
    public void createUserSuccess() {

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals(0, responseUser.getId());
        assertEquals("test", responseUser.getUsername());
        assertEquals("ThisIsHashed", responseUser.getPassword());

    }

    @Test
    public void createUserWithPasswordError() {

        createUserRequest.setConfirmPassword("wrong");
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void findUserById() {
        User responseUser = userController.findById(Long.valueOf(1L)).getBody();
        assertNotNull(responseUser);
        assertEquals(user, responseUser);

    }

    @Test
    public void findUserByName() {
        User responseUser = userController.findByUserName("testUser").getBody();
        assertNotNull(responseUser);
        assertEquals(user, responseUser);
    }


}
